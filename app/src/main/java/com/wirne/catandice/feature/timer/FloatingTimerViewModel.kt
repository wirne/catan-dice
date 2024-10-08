package com.wirne.catandice.feature.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wirne.catandice.data.repository.GameStateRepository
import com.wirne.catandice.data.repository.TimerRepository
import com.wirne.catandice.feature.timer.FloatingTimerContract.Event
import com.wirne.catandice.feature.timer.FloatingTimerContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class FloatingTimerViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    gameStateRepository: GameStateRepository,
) : ViewModel(), FloatingTimerContract {
    private var countDownJob: Job? = null
    private val isTimerRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val shouldVibrate: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val state: StateFlow<State> =
        combine(
            timerRepository.timer,
            gameStateRepository.gameState,
            isTimerRunning,
            shouldVibrate,
        ) { timer, gameState, running, shouldVibrate ->

            if (gameState.rollHistory.isEmpty() || !timer.enabled) {
                stopCounter()
            }

            State(
                timeLeft = timer.timeLeft,
                running = running,
                enabled = timer.enabled,
                gotHistory = gameState.rollHistory.isNotEmpty(),
                shouldVibrate = shouldVibrate,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = State.Initial,
        )

    init {
        viewModelScope.launch {
            state.distinctUntilChanged { old, new ->
                val isFirstRoll = !old.gotHistory && new.gotHistory
                val isRollAfterTimeout = old.timeout && !new.timeout
                if (isFirstRoll || isRollAfterTimeout) {
                    startTimer()
                }
                old == new
            }.collect()
        }
    }

    override fun event(event: Event) {
        viewModelScope.launch {
            when (event) {
                Event.OnPauseTimer -> pauseTimer()
                Event.OnPlayTimer -> startTimer()
                Event.OnStopTimer -> stopTimer()
                Event.OnVibrate -> onVibrate()
            }
        }
    }

    private fun onVibrate() {
        shouldVibrate.value = false
    }

    private fun startTimer() {
        if (state.value.running || state.value.timeout) {
            return
        }
        startCounter()
    }

    private suspend fun stopTimer() {
        stopCounter()
        timerRepository.resetTimer()
    }

    private fun pauseTimer() {
        stopCounter()
    }

    private fun stopCounter() {
        isTimerRunning.value = false
        countDownJob?.cancel()
    }

    private fun startCounter() {
        val job = countDownJob
        if (job != null && job.isActive) {
            return
        }

        countDownJob?.cancel()
        countDownJob =
            viewModelScope.launch {
                while (!state.value.timeout) {
                    delay(1.seconds)
                    timerRepository.updateTimeLeft(timeLeft = state.value.timeLeft.minus(1.seconds))
                }
                isTimerRunning.value = false
                shouldVibrate.value = true
                cancel()
            }
    }
}
