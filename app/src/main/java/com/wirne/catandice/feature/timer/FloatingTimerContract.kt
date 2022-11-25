package com.wirne.catandice.feature.timer

import com.wirne.catandice.common.ViewModelContract
import kotlin.time.Duration

interface FloatingTimerContract : ViewModelContract<FloatingTimerContract.Event, FloatingTimerContract.Effect, FloatingTimerContract.State> {

    sealed class Event {
        object OnPauseTimer : Event()
        object OnPlayTimer : Event()
        object OnStopTimer : Event()
    }

    sealed class Effect {
        object Timeout : Effect()
    }

    data class State(
        val timeLeft: Duration,
        val running: Boolean,
        val enabled: Boolean,
        val gotHistory: Boolean
    ) {
        val finished: Boolean = timeLeft == Duration.ZERO

        companion object {
            val Initial = State(
                timeLeft = Duration.ZERO,
                running = false,
                enabled = false,
                gotHistory = false
            )
        }
    }
}
