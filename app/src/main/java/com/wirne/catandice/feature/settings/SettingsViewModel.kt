package com.wirne.catandice.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wirne.catandice.data.repository.SettingsRepository
import com.wirne.catandice.data.repository.TimerRepository
import com.wirne.catandice.feature.settings.SettingsContract.Event
import com.wirne.catandice.feature.settings.SettingsContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val timerRepository: TimerRepository,
) : ViewModel(), SettingsContract {
    override val state: StateFlow<State> =
        combine(
            settingsRepository.settings,
            timerRepository.timer,
        ) { settings, timer ->
            State(
                randomPercentage = settings.randomPercentage,
                citiesAndKnightsEnabled = settings.citiesAndKnightsEnabled,
                time = timer.time,
                timerEnabled = timer.enabled,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = State.Initial,
        )

    override fun event(event: Event) {
        viewModelScope.launch {
            when (event) {
                Event.OnTimerEnabledClicked -> onTimerEnabledClicked()
                Event.OnCitiesAndKnightsEnabledClicked -> onCitiesAndKnightsEnabledClicked()
                is Event.OnTimeSelected -> onTimeSelected(event.time)
                is Event.OnPercentageSelected -> onPercentageSelected(event.percentage)
            }
        }
    }

    private suspend fun onPercentageSelected(percentage: Int) {
        settingsRepository.updateRandomPercentage(percentage)
    }

    private suspend fun onCitiesAndKnightsEnabledClicked() {
        settingsRepository.toggleCitiesAndKnightsEnabled()
    }

    private suspend fun onTimerEnabledClicked() {
        timerRepository.toggleTimerEnabled()
    }

    private suspend fun onTimeSelected(time: Duration) {
        timerRepository.updateTime(time)
    }
}
