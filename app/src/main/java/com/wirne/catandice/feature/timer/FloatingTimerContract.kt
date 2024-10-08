package com.wirne.catandice.feature.timer

import com.wirne.catandice.common.ViewModelContract
import kotlin.time.Duration

interface FloatingTimerContract : ViewModelContract<FloatingTimerContract.Event, FloatingTimerContract.State> {
    sealed class Event {
        data object OnPauseTimer : Event()

        data object OnPlayTimer : Event()

        data object OnStopTimer : Event()

        data object OnVibrate : Event()
    }

    data class State(
        val timeLeft: Duration,
        val running: Boolean,
        val enabled: Boolean,
        val gotHistory: Boolean,
        val shouldVibrate: Boolean,
    ) {
        val timeout: Boolean = timeLeft == Duration.ZERO

        companion object {
            val Initial =
                State(
                    timeLeft = Duration.ZERO,
                    running = false,
                    enabled = false,
                    gotHistory = false,
                    shouldVibrate = false,
                )
        }
    }
}
