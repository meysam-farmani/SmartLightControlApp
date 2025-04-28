package com.example.smartlight.presentation.light

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartlight.domain.model.LightState
import com.example.smartlight.domain.usecase.AdjustBrightnessUseCase
import com.example.smartlight.domain.usecase.ObserveLightStateUseCase
import com.example.smartlight.domain.usecase.TurnOffLightUseCase
import com.example.smartlight.domain.usecase.TurnOnLightUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LightViewModel @Inject constructor(
    private val turnOnLightUseCase: TurnOnLightUseCase,
    private val turnOffLightUseCase: TurnOffLightUseCase,
    private val adjustBrightnessUseCase: AdjustBrightnessUseCase,
    private val observeLightStateUseCase: ObserveLightStateUseCase
) : ViewModel() {

    private val _lightState = MutableStateFlow(LightState())
    val lightState: StateFlow<LightState> = _lightState.asStateFlow()

    init {
        viewModelScope.launch {
            observeLightStateUseCase().collect { state ->
                _lightState.value = state
            }
        }
    }

    fun turnOnLight() {
        viewModelScope.launch {
            turnOnLightUseCase()
            _lightState.update { it.copy(isOn = true) }
        }
    }

    fun turnOffLight() {
        viewModelScope.launch {
            turnOffLightUseCase()
            _lightState.update { it.copy(isOn = false) }
        }
    }

    fun adjustBrightness(brightness: Int) {
        viewModelScope.launch {
            adjustBrightnessUseCase(brightness)
            _lightState.update { it.copy(brightness = brightness) }
        }
    }
}
