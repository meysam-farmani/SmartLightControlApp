# 💡 SmartLightControlApp

A simple Android app to simulate controlling a Smart Light Bulb using MQTT protocol.

---

## ✨ Features
- ✅ Toggle the Smart Light ON/OFF
- ✅ Adjust Light Brightness (0% - 100%)
- ✅ Real-Time Light State Updates from MQTT
- ✅ Offline-friendly with UI Auto-Update for Testing
- ✅ Clean MVVM Architecture with UseCases, Repository, Hilt DI, and Jetpack Compose

---

## 🛠️ Tech Stack
- 💻 Kotlin
- 🏛️ MVVM (Model-View-ViewModel)
- 🎯 Clean Architecture (Repository, UseCases)
- 🎨 Jetpack Compose (Modern Declarative UI)
- 🧩 Dagger Hilt (Dependency Injection)
- 📡 Eclipse Paho MQTT Client (MqttAsyncClient)
- 🛰️ Mosquitto Public Broker (`test.mosquitto.org`)

---

## 📡 MQTT Setup

- **Broker URL:**  
  `tcp://test.mosquitto.org:1883`
  
- **Topics Used:**
  - **Publish Commands** (App ➡️ Device):  
    `smartlight/command`
    
  - **Listen for State Updates** (Device ➡️ App):  
    `smartlight/state`

---

## 🔀 MQTT Message Flow

### 1. 📤 App Publishing Commands
When the user interacts (toggle switch, change brightness):

| Action | Topic | Example Payload |
|:---|:---|:---|
| Turn Light ON | `smartlight/command` | `{ "command": "turn_on" }` |
| Turn Light OFF | `smartlight/command` | `{ "command": "turn_off" }` |
| Set Brightness | `smartlight/command` | `{ "command": "set_brightness", "value": 80 }` |

---

### 2. 📥 App Receiving Light State
The App **subscribes** to the light's current state:

| Topic | Expected Payload Example |
|:---|:---|
| `smartlight/state` | `{ "isOn": true, "brightness": 80 }` |

✅ This payload updates the UI automatically.

---

## 🧪 Testing Without Real Bulb

You can manually simulate a bulb using **MQTT Explorer**:

1. **Connect to:** `test.mosquitto.org:1883`
2. **Subscribe** to topic:  
   `smartlight/#`
3. **Manually Publish** to `smartlight/state`:

Example Light ON:
```json
{ "isOn": true, "brightness": 90 }
