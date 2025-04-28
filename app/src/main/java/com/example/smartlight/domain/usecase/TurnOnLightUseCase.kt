package com.example.smartlight.domain.usecase

import com.example.smartlight.data.repository.LightRepository
import javax.inject.Inject

class TurnOnLightUseCase @Inject constructor(
    private val repository: LightRepository
) {
    suspend operator fun invoke() {
        repository.turnOnLight()
    }
}