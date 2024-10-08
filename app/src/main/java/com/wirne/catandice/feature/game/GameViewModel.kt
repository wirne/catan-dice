package com.wirne.catandice.feature.game

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wirne.catandice.data.model.CitiesAndKnightsDiceOutcome
import com.wirne.catandice.data.model.ShipState
import com.wirne.catandice.data.model.TwoDiceOutcome
import com.wirne.catandice.data.repository.GameStateRepository
import com.wirne.catandice.data.repository.SettingsRepository
import com.wirne.catandice.data.repository.TimerRepository
import com.wirne.catandice.feature.game.GameContract.Event
import com.wirne.catandice.feature.game.GameContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(
    settingsRepository: SettingsRepository,
    private val gameStateRepository: GameStateRepository,
    private val timerRepository: TimerRepository,
) : ViewModel(), GameContract {
    override val state = combine(
        settingsRepository.settings,
        gameStateRepository.gameState,
    ) { settings, gameState ->
        State(
            diceRollHistory = gameState.rollHistory,
            randomPercentage = settings.randomPercentage,
            citiesAndKnightsEnabled = settings.citiesAndKnightsEnabled,
            shipState = gameState.shipState,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue =
        State(
            diceRollHistory = emptyList(),
            randomPercentage = 0,
            citiesAndKnightsEnabled = false,
            shipState = ShipState.One,
        ),
    )

    override fun event(event: Event) {
        viewModelScope.launch {
            when (event) {
                Event.Roll -> roll()
                Event.Reset -> reset()
                is Event.OnShipStateChange -> updateShipState(event.state)
            }
        }
    }

    private suspend fun reset() {
        gameStateRepository.reset()
        timerRepository.resetTimer()
    }

    private suspend fun roll() {
        val shouldTakeRandom = Random.nextInt(until = 100) < state.value.randomPercentage

        val twoDiceOutcome = if (shouldTakeRandom) {
            TwoDiceOutcome.entries.random()
        } else {
            gameStateRepository.takeRandomTwoDiceOutcome()
        }

        timerRepository.resetTimer()

        val citiesAndKnightsOutcome = CitiesAndKnightsDiceOutcome.entries.random()

        if (citiesAndKnightsOutcome.isShip() && state.value.citiesAndKnightsEnabled) {
            gameStateRepository.updateShipState(state.value.shipState.next())
        }

        gameStateRepository.addToHistory(
            twoDiceOutcome = twoDiceOutcome,
            citiesAndKnightsDiceOutcome = citiesAndKnightsOutcome,
            random = shouldTakeRandom,
        )
    }

    private suspend fun updateShipState(state: ShipState) {
        gameStateRepository.updateShipState(state)
    }
}

@Immutable
data class DiceRoll(
    val twoDiceOutcome: TwoDiceOutcome,
    val citiesAndKnightsDiceOutcome: CitiesAndKnightsDiceOutcome,
    val turn: Int,
    val random: Boolean,
)
