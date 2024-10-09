package com.wirne.catandice.feature.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.wirne.catandice.ui.theme.CDColor
import com.wirne.catandice.ui.theme.CDTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun TimerSelector(
    time: Duration,
    onTimeSelected: (Duration) -> Unit,
) {
    Column {
        var selectedTime by remember(time) { mutableStateOf(time) }

        Text(text = "Timer: $selectedTime")

        Slider(
            value = selectedTime.inWholeSeconds.toFloat(),
            onValueChange = {
                selectedTime = it.toInt().seconds
            },
            onValueChangeFinished = {
                if (selectedTime != time) {
                    onTimeSelected(selectedTime)
                }
            },
            // up to 10 minutes
            valueRange = 10f.rangeTo(600f),
            colors =
                SliderDefaults.colors(
                    thumbColor = CDColor.Yellow,
                    activeTrackColor = CDColor.Red,
                ),
        )
    }
}

@Composable
@Preview
private fun Preview() {
    CDTheme {
        Surface {
            TimerSelector(
                time = 100.seconds,
                onTimeSelected = { },
            )
        }
    }
}
