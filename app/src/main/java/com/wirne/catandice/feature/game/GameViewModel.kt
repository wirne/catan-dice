package com.wirne.catandice.feature.game

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wirne.catandice.data.model.CitiesAndKnightsDiceOutcome
import com.wirne.catandice.data.model.TwoDiceOutcome
import com.wirne.catandice.feature.game.GameContract.Event
import com.wirne.catandice.feature.game.GameContract.Effect
import com.wirne.catandice.feature.game.GameContract.State
import com.wirne.catandice.data.repository.GameStateRepository
import com.wirne.catandice.data.repository.SettingsRepository
import com.wirne.catandice.data.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class GameViewModel @Inject constructor(
    settingsRepository: SettingsRepository,
    private val gameStateRepository: GameStateRepository,
    private val timerRepository: TimerRepository
) : ViewModel(), GameContract {

    override val state = combine(
        settingsRepository.settings,
        gameStateRepository.gameState,
    ) { settings, gameState ->
        State(
            diceRollHistory = gameState.rollHistory,
            randomPercentage = settings.randomPercentage,
            citiesAndKnightsEnabled = settings.citiesAndKnightsEnabled,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            diceRollHistory = emptyList(),
            randomPercentage = 0,
            citiesAndKnightsEnabled = false
        )
    )

    private val effectChannel = Channel<Effect>(UNLIMITED)
    override val effect: Flow<Effect> = effectChannel.receiveAsFlow()

    override fun event(event: Event) {
        viewModelScope.launch {
            when (event) {
                Event.Roll -> roll()
                Event.Reset -> reset()
            }
        }
    }

    private suspend fun reset() {
        gameStateRepository.reset()
        timerRepository.resetTimer()
    }

    private suspend fun roll() {
        val shouldTakeRandom = Random.nextInt(
            until = 100
        ) < state.value.randomPercentage

        val twoDiceOutcome = if (shouldTakeRandom) {
            TwoDiceOutcome.values().random()
        } else {
            gameStateRepository.takeRandomTwoDiceOutcome()
        }

        timerRepository.resetTimer()

        gameStateRepository.addToHistory(
            twoDiceOutcome = twoDiceOutcome,
            knightsAndCitiesDiceOutcome = CitiesAndKnightsDiceOutcome.values().random(),
            random = shouldTakeRandom
        )
    }
}

@Immutable
data class DiceRoll(
    val twoDiceOutcome: TwoDiceOutcome,
    val citiesAndKnightsDiceOutcome: CitiesAndKnightsDiceOutcome,
    val turn: Int,
    val random: Boolean
)
