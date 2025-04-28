package com.example.smartlight.data.mqtt

import android.util.Log
import com.example.smartlight.domain.model.LightState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttManager {

    companion object {
        private const val TAG = "MqttManager"
        private const val SERVER_URI = "tcp://test.mosquitto.org:1883"
        private const val CLIENT_ID = "mqtt-explorer-49ef11a7"
        private const val COMMAND_TOPIC = "smartlight/command"
        private const val STATE_TOPIC = "smartlight/state"
    }

    private val mqttClient = MqttAsyncClient(SERVER_URI, CLIENT_ID, MemoryPersistence())

    private var currentState = LightState()
    private var isConnected = false

    init {
        connect()
    }

    private fun connect() {
        val options = MqttConnectOptions().apply {
            isAutomaticReconnect = true
            isCleanSession = true
        }

        mqttClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "Connection lost: ${cause?.message}")
                isConnected = false
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                message?.toString()?.let { parseStateMessage(it) }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {}
        })

        mqttClient.connect(options, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(TAG, "MQTT connection accepted, waiting for real connection...")
                subscribeToStateTopicWithRetry()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.e(TAG, "MQTT Connection failed: ${exception?.message}")
            }
        })
    }

    private fun subscribeToStateTopicWithRetry() {
        CoroutineScope(Dispatchers.IO).launch {
            repeat(10) { attempt ->
                if (mqttClient.isConnected) {
                    isConnected = true
                    try {
                        mqttClient.subscribe(STATE_TOPIC, 1)
                        Log.d(TAG, "Subscribed to STATE_TOPIC after $attempt retries")
                    } catch (e: Exception) {
                        Log.e(TAG, "Subscription failed: ${e.message}")
                    }
                    return@launch
                } else {
                    Log.d(TAG, "Waiting for real connection... attempt $attempt")
                    delay(200)
                }
            }
            Log.e(TAG, "Failed to subscribe after retries")
        }
    }

    fun observeLightState(): Flow<LightState> = callbackFlow {
        val callback = object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Log.e(TAG, "Connection Lost observeLightState")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                message?.toString()?.let {
                    parseStateMessage(it)
                    trySend(currentState)
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.e(TAG, "Delivery Complete")
            }
        }

        mqttClient.setCallback(callback)

        awaitClose {
            if (mqttClient.isConnected) {
                mqttClient.disconnect()
            }
        }
    }

    private fun parseStateMessage(payload: String) {
        try {
            val parts = payload
                .replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .split(",")

            val isOn = parts[0].split(":")[1].toBoolean()
            val brightness = parts[1].split(":")[1].toInt()

            currentState = LightState(isOn, brightness)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse state message: $payload")
        }
    }

    suspend fun publishTurnOn() {
        publishMessage(COMMAND_TOPIC, "{\"command\":\"turn_on\"}")
    }

    suspend fun publishTurnOff() {
        publishMessage(COMMAND_TOPIC, "{\"command\":\"turn_off\"}")
    }

    suspend fun publishBrightness(brightness: Int) {
        publishMessage(COMMAND_TOPIC, "{\"command\":\"set_brightness\",\"value\":$brightness}")
    }

    private fun publishMessage(topic: String, message: String) {
        try {
            if (isConnected && mqttClient.isConnected) {
                val mqttMessage = MqttMessage(message.toByteArray())
                mqttClient.publish(topic, mqttMessage)
                Log.d(TAG, "Published message to $topic: $message")
            } else {
                Log.e(TAG, "Cannot publish — MQTT not connected")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to publish: ${e.message}")
        }
    }
}
