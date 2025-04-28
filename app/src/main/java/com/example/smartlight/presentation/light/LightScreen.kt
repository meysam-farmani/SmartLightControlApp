package com.example.smartlight.presentation.light

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LightScreen(viewModel: LightViewModel = hiltViewModel()) {
    val lightState by viewModel.lightState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (lightState.isOn) "Light is ON" else "Light is OFF",
            style = MaterialTheme.typography.headlineMedium
        )

        Slider(
            value = lightState.brightness.toFloat(),
            onValueChange = { viewModel.adjustBrightness(it.toInt()) },
            valueRange = 0f..100f,
            steps = 9,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Brightness: ${lightState.brightness}%",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Switch(
                checked = lightState.isOn,
                onCheckedChange = { isChecked ->
                    if (isChecked) viewModel.turnOnLight() else viewModel.turnOffLight()
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFFFFEB3B),
                    uncheckedThumbColor = Color.Gray
                )
            )

            Text(
                text = if (lightState.isOn) "Turn OFF" else "Turn ON",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
