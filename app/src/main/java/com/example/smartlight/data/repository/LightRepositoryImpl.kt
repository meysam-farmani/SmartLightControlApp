package com.example.smartlight.data.repository

import com.example.smartlight.data.mqtt.MqttManager
import com.example.smartlight.domain.model.LightState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LightRepositoryImpl @Inject constructor(
    private val mqttManager: MqttManager
) : LightRepository {

    override fun observeLightState(): Flow<LightState> {
        return mqttManager.observeLightState()
    }

    override suspend fun turnOnLight() {
        mqttManager.publishTurnOn()
    }

    override suspend fun turnOffLight() {
        mqttManager.publishTurnOff()
    }

    override suspend fun adjustBrightness(brightness: Int) {
        mqttManager.publishBrightness(brightness)
    }
}