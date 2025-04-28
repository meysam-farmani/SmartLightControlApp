package com.example.smartlight.data.repository

import com.example.smartlight.domain.model.LightState
import kotlinx.coroutines.flow.Flow

interface LightRepository {
    fun observeLightState(): Flow<LightState>
    suspend fun turnOnLight()
    suspend fun turnOffLight()
    suspend fun adjustBrightness(brightness: Int)
}