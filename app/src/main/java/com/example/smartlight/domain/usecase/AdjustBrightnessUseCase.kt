package com.example.smartlight.domain.usecase

import com.example.smartlight.data.repository.LightRepository
import javax.inject.Inject

class AdjustBrightnessUseCase @Inject constructor(
    private val repository: LightRepository
) {
    suspend operator fun invoke(brightness: Int) {
        repository.adjustBrightness(brightness)
    }
}