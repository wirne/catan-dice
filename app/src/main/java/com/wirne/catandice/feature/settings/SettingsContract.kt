package com.wirne.catandice.feature.settings

import com.wirne.catandice.common.ViewModelContract
import kotlin.time.Duration

interface SettingsContract :
    ViewModelContract<SettingsContract.Event, SettingsContract.Effect, SettingsContract.State> {
    sealed class Event {
        data class OnPercentageSelected(val percentage: Int) : Event()

        data class OnTimeSelected(val time: Duration) : Event()

        object OnCitiesAndKnightsEnabledClicked : Event()

        object OnTimerEnabledClicked : Event()
    }

    object Effect

    data class State(
        val randomPercentage: Int,
        val citiesAndKnightsEnabled: Boolean,
        val time: Duration,
        val timerEnabled: Boolean,
    ) {
        companion object {
            val Initial =
                State(
                    randomPercentage = 0,
                    citiesAndKnightsEnabled = false,
                    time = Duration.ZERO,
                    timerEnabled = false,
                )
        }
    }
}
