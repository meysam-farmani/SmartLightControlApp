package com.example.smartlight.domain.usecase

import com.example.smartlight.domain.model.LightState
import com.example.smartlight.data.repository.LightRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLightStateUseCase @Inject constructor(
    private val repository: LightRepository
) {
    operator fun invoke(): Flow<LightState> {
        return repository.observeLightState()
    }
}