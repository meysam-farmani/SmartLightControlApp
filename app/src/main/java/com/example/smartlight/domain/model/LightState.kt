package com.example.smartlight.domain.model

data class LightState(
    val isOn: Boolean = false,
    val brightness: Int = 0
)