package com.wirne.catandice.feature.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wirne.catandice.data.repository.GameStateRepository
import com.wirne.catandice.data.repository.TimerRepository
import com.wirne.catandice.feature.timer.FloatingTimerContract.Effect
import com.wirne.catandice.feature.timer.FloatingTimerContract.Event
import com.wirne.catandice.feature.timer.FloatingTimerContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class FloatingTimerViewModel
    @Inject
    constructor(
        private val timerRepository: TimerRepository,
        gameStateRepository: GameStateRepository,
    ) : ViewModel(), FloatingTimerContract {
        private var countDownJob: Job? = null
        private val isTimerRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)

        override val state: StateFlow<State> =
            combine(
                timerRepository.timer,
                gameStateRepository.gameState,
                isTimerRunning,
            ) { timer, gameState, running ->

                if (gameState.rollHistory.isEmpty() || !timer.enabled) {
                    stopCounter()
                }

                State(
                    timeLeft = timer.timeLeft,
                    running = running,
                    enabled = timer.enabled,
                    gotHistory = gameState.rollHistory.isNotEmpty(),
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
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
                }
            }
        }

        private val effectChannel = Channel<Effect>(Channel.UNLIMITED)
        override val effect: Flow<Effect> = effectChannel.receiveAsFlow()

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
                    effectChannel.send(Effect.Timeout)
                    cancel()
                }
        }
    }
