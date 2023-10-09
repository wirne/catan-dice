package com.wirne.catandice.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.wirne.catandice.data.model.GameState
import com.wirne.catandice.data.model.CitiesAndKnightsDiceOutcome
import com.wirne.catandice.data.model.ShipState
import com.wirne.catandice.data.model.TwoDiceOutcome
import com.wirne.catandice.datastore.*
import com.wirne.catandice.feature.game.DiceRoll
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val DATA_STORE_FILE_NAME = "persisted_game_state.proto"

private val Context.gameStateStore: DataStore<PersistedGameState> by dataStore(
    fileName = DATA_STORE_FILE_NAME,
    serializer = GameStateSerializer
)

@Singleton
class GameStateRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val gameState: Flow<GameState> = context.gameStateStore.data
        .map { it.toGameState() }

    suspend fun reset() {
        context.gameStateStore.updateData {
            PersistedGameState.getDefaultInstance()
        }
    }

    suspend fun updateShipState(state: ShipState) {
        context.gameStateStore.updateData {
            it.toBuilder()
                .setShipState(state.toPersistedShipState())
                .build()
        }
    }

    private suspend fun resetTwoDiceEntropy() {
        context.gameStateStore.updateData {
            it.toBuilder()
                .clearTwoDiceOutcomes()
                .addAllTwoDiceOutcomes(
                    TwoDiceOutcome.values().map { it.toPersistedTwoDiceOutcome() })
                .build()
        }
    }

    suspend fun takeRandomTwoDiceOutcome(): TwoDiceOutcome {
        if (context.gameStateStore.data.first().twoDiceOutcomesList.isEmpty()) {
            resetTwoDiceEntropy()
        }
        var randomTwoDiceOutcome: TwoDiceOutcome = TwoDiceOutcome.values().random()

        context.gameStateStore.updateData {
            val persistedTwoDiceOutcomes = it.twoDiceOutcomesList.toList()
            val randomPersistedTwoDiceOutcome = persistedTwoDiceOutcomes.random()
            randomTwoDiceOutcome = randomPersistedTwoDiceOutcome.toTwoDiceOutcomeOrNull() ?: randomTwoDiceOutcome

            it.toBuilder()
                .clearTwoDiceOutcomes()
                .addAllTwoDiceOutcomes(persistedTwoDiceOutcomes.minus(randomPersistedTwoDiceOutcome))
                .build()
        }

        return randomTwoDiceOutcome
    }

    suspend fun addToHistory(
        twoDiceOutcome: TwoDiceOutcome,
        citiesAndKnightsDiceOutcome: CitiesAndKnightsDiceOutcome,
        random: Boolean
    ) {
        context.gameStateStore.updateData {
            val turn = (it.historyList.lastOrNull()?.turn ?: 0) + 1
            it.toBuilder()
                .addHistory(
                    PersistedDiceRoll.newBuilder()
                        .setTurn(turn)
                        .setCitiesAndKnightsDiceOutcome(citiesAndKnightsDiceOutcome.toPersistedCitiesAndKnightsDiceOutcome())
                        .setTwoDiceOutcome(twoDiceOutcome.toPersistedTwoDiceOutcome())
                        .setRandom(random)
                        .build()
                )
                .build()

        }
    }
}

private fun CitiesAndKnightsDiceOutcome.toPersistedCitiesAndKnightsDiceOutcome(): PersistedCitiesAndKnightsDiceOutcome =
    when (this) {
        CitiesAndKnightsDiceOutcome.Ship1 -> PersistedCitiesAndKnightsDiceOutcome.Ship1
        CitiesAndKnightsDiceOutcome.Ship2 -> PersistedCitiesAndKnightsDiceOutcome.Ship2
        CitiesAndKnightsDiceOutcome.Ship3 -> PersistedCitiesAndKnightsDiceOutcome.Ship3
        CitiesAndKnightsDiceOutcome.Blue -> PersistedCitiesAndKnightsDiceOutcome.Blue
        CitiesAndKnightsDiceOutcome.Yellow -> PersistedCitiesAndKnightsDiceOutcome.Yellow
        CitiesAndKnightsDiceOutcome.Green -> PersistedCitiesAndKnightsDiceOutcome.Green
    }

private fun PersistedGameState.toGameState(): GameState {
    val history = historyList.mapNotNull {
        DiceRoll(
            twoDiceOutcome = it.twoDiceOutcome.toTwoDiceOutcomeOrNull() ?: return@mapNotNull null,
            citiesAndKnightsDiceOutcome = it.citiesAndKnightsDiceOutcome.toCitiesAndKnightsDiceOutcomeOrNull()
                ?: return@mapNotNull null,
            turn = it.turn,
            random = it.random
        )
    }

    return GameState(
        rollHistory = history,
        twoDiceEntropy = twoDiceOutcomesList.mapNotNull { it.toTwoDiceOutcomeOrNull() },
        shipState = shipState.toShipState()
    )
}

private fun PersistedShipState.toShipState(): ShipState =
    when (this) {
        PersistedShipState.One,
        PersistedShipState.UNRECOGNIZED -> ShipState.One

        PersistedShipState.Two -> ShipState.Two
        PersistedShipState.Three -> ShipState.Three
        PersistedShipState.Four -> ShipState.Four
        PersistedShipState.Five -> ShipState.Five
        PersistedShipState.Six -> ShipState.Six
        PersistedShipState.Seven -> ShipState.Seven
        PersistedShipState.Eight -> ShipState.Eight
    }

private fun ShipState.toPersistedShipState(): PersistedShipState =
    when (this) {
        ShipState.One -> PersistedShipState.One
        ShipState.Two -> PersistedShipState.Two
        ShipState.Three -> PersistedShipState.Three
        ShipState.Four -> PersistedShipState.Four
        ShipState.Five -> PersistedShipState.Five
        ShipState.Six -> PersistedShipState.Six
        ShipState.Seven -> PersistedShipState.Seven
        ShipState.Eight -> PersistedShipState.Eight
    }

private fun PersistedCitiesAndKnightsDiceOutcome.toCitiesAndKnightsDiceOutcomeOrNull(): CitiesAndKnightsDiceOutcome? =
    when (this) {
        PersistedCitiesAndKnightsDiceOutcome.Ship1 -> CitiesAndKnightsDiceOutcome.Ship1
        PersistedCitiesAndKnightsDiceOutcome.Ship2 -> CitiesAndKnightsDiceOutcome.Ship2
        PersistedCitiesAndKnightsDiceOutcome.Ship3 -> CitiesAndKnightsDiceOutcome.Ship3
        PersistedCitiesAndKnightsDiceOutcome.Blue -> CitiesAndKnightsDiceOutcome.Blue
        PersistedCitiesAndKnightsDiceOutcome.Yellow -> CitiesAndKnightsDiceOutcome.Yellow
        PersistedCitiesAndKnightsDiceOutcome.Green -> CitiesAndKnightsDiceOutcome.Green
        PersistedCitiesAndKnightsDiceOutcome.UNRECOGNIZED -> null
    }

private fun PersistedTwoDiceOutcome.toTwoDiceOutcomeOrNull(): TwoDiceOutcome? =
    when (this) {
        PersistedTwoDiceOutcome.OneOne -> TwoDiceOutcome.OneOne
        PersistedTwoDiceOutcome.TwoOne -> TwoDiceOutcome.TwoOne
        PersistedTwoDiceOutcome.OneTwo -> TwoDiceOutcome.OneTwo
        PersistedTwoDiceOutcome.ThreeOne -> TwoDiceOutcome.ThreeOne
        PersistedTwoDiceOutcome.TwoTwo -> TwoDiceOutcome.TwoTwo
        PersistedTwoDiceOutcome.OneThree -> TwoDiceOutcome.OneThree
        PersistedTwoDiceOutcome.FourOne -> TwoDiceOutcome.FourOne
        PersistedTwoDiceOutcome.ThreeTwo -> TwoDiceOutcome.ThreeTwo
        PersistedTwoDiceOutcome.TwoThree -> TwoDiceOutcome.TwoThree
        PersistedTwoDiceOutcome.OneFour -> TwoDiceOutcome.OneFour
        PersistedTwoDiceOutcome.FiveOne -> TwoDiceOutcome.FiveOne
        PersistedTwoDiceOutcome.FourTwo -> TwoDiceOutcome.FourTwo
        PersistedTwoDiceOutcome.ThreeThree -> TwoDiceOutcome.ThreeThree
        PersistedTwoDiceOutcome.TwoFour -> TwoDiceOutcome.TwoFour
        PersistedTwoDiceOutcome.OneFive -> TwoDiceOutcome.OneFive
        PersistedTwoDiceOutcome.SixOne -> TwoDiceOutcome.SixOne
        PersistedTwoDiceOutcome.FiveTwo -> TwoDiceOutcome.FiveTwo
        PersistedTwoDiceOutcome.FourThree -> TwoDiceOutcome.FourThree
        PersistedTwoDiceOutcome.ThreeFour -> TwoDiceOutcome.ThreeFour
        PersistedTwoDiceOutcome.TwoFive -> TwoDiceOutcome.TwoFive
        PersistedTwoDiceOutcome.OneSix -> TwoDiceOutcome.OneSix
        PersistedTwoDiceOutcome.SixTwo -> TwoDiceOutcome.SixTwo
        PersistedTwoDiceOutcome.FiveThree -> TwoDiceOutcome.FiveThree
        PersistedTwoDiceOutcome.FourFour -> TwoDiceOutcome.FourFour
        PersistedTwoDiceOutcome.ThreeFive -> TwoDiceOutcome.ThreeFive
        PersistedTwoDiceOutcome.TwoSix -> TwoDiceOutcome.TwoSix
        PersistedTwoDiceOutcome.SixThree -> TwoDiceOutcome.SixThree
        PersistedTwoDiceOutcome.FiveFour -> TwoDiceOutcome.FiveFour
        PersistedTwoDiceOutcome.FourFive -> TwoDiceOutcome.FourFive
        PersistedTwoDiceOutcome.ThreeSix -> TwoDiceOutcome.ThreeSix
        PersistedTwoDiceOutcome.SixFour -> TwoDiceOutcome.SixFour
        PersistedTwoDiceOutcome.FiveFive -> TwoDiceOutcome.FiveFive
        PersistedTwoDiceOutcome.FourSix -> TwoDiceOutcome.FourSix
        PersistedTwoDiceOutcome.SixFive -> TwoDiceOutcome.SixFive
        PersistedTwoDiceOutcome.FiveSix -> TwoDiceOutcome.FiveSix
        PersistedTwoDiceOutcome.SixSix -> TwoDiceOutcome.SixSix
        PersistedTwoDiceOutcome.UNRECOGNIZED -> null
    }


private fun TwoDiceOutcome.toPersistedTwoDiceOutcome(): PersistedTwoDiceOutcome =
    when (this) {
        TwoDiceOutcome.OneOne -> PersistedTwoDiceOutcome.OneOne
        TwoDiceOutcome.TwoOne -> PersistedTwoDiceOutcome.TwoOne
        TwoDiceOutcome.OneTwo -> PersistedTwoDiceOutcome.OneTwo
        TwoDiceOutcome.ThreeOne -> PersistedTwoDiceOutcome.ThreeOne
        TwoDiceOutcome.TwoTwo -> PersistedTwoDiceOutcome.TwoTwo
        TwoDiceOutcome.OneThree -> PersistedTwoDiceOutcome.OneThree
        TwoDiceOutcome.FourOne -> PersistedTwoDiceOutcome.FourOne
        TwoDiceOutcome.ThreeTwo -> PersistedTwoDiceOutcome.ThreeTwo
        TwoDiceOutcome.TwoThree -> PersistedTwoDiceOutcome.TwoThree
        TwoDiceOutcome.OneFour -> PersistedTwoDiceOutcome.OneFour
        TwoDiceOutcome.FiveOne -> PersistedTwoDiceOutcome.FiveOne
        TwoDiceOutcome.FourTwo -> PersistedTwoDiceOutcome.FourTwo
        TwoDiceOutcome.ThreeThree -> PersistedTwoDiceOutcome.ThreeThree
        TwoDiceOutcome.TwoFour -> PersistedTwoDiceOutcome.TwoFour
        TwoDiceOutcome.OneFive -> PersistedTwoDiceOutcome.OneFive
        TwoDiceOutcome.SixOne -> PersistedTwoDiceOutcome.SixOne
        TwoDiceOutcome.FiveTwo -> PersistedTwoDiceOutcome.FiveTwo
        TwoDiceOutcome.FourThree -> PersistedTwoDiceOutcome.FourThree
        TwoDiceOutcome.ThreeFour -> PersistedTwoDiceOutcome.ThreeFour
        TwoDiceOutcome.TwoFive -> PersistedTwoDiceOutcome.TwoFive
        TwoDiceOutcome.OneSix -> PersistedTwoDiceOutcome.OneSix
        TwoDiceOutcome.SixTwo -> PersistedTwoDiceOutcome.SixTwo
        TwoDiceOutcome.FiveThree -> PersistedTwoDiceOutcome.FiveThree
        TwoDiceOutcome.FourFour -> PersistedTwoDiceOutcome.FourFour
        TwoDiceOutcome.ThreeFive -> PersistedTwoDiceOutcome.ThreeFive
        TwoDiceOutcome.TwoSix -> PersistedTwoDiceOutcome.TwoSix
        TwoDiceOutcome.SixThree -> PersistedTwoDiceOutcome.SixThree
        TwoDiceOutcome.FiveFour -> PersistedTwoDiceOutcome.FiveFour
        TwoDiceOutcome.FourFive -> PersistedTwoDiceOutcome.FourFive
        TwoDiceOutcome.ThreeSix -> PersistedTwoDiceOutcome.ThreeSix
        TwoDiceOutcome.SixFour -> PersistedTwoDiceOutcome.SixFour
        TwoDiceOutcome.FiveFive -> PersistedTwoDiceOutcome.FiveFive
        TwoDiceOutcome.FourSix -> PersistedTwoDiceOutcome.FourSix
        TwoDiceOutcome.SixFive -> PersistedTwoDiceOutcome.SixFive
        TwoDiceOutcome.FiveSix -> PersistedTwoDiceOutcome.FiveSix
        TwoDiceOutcome.SixSix -> PersistedTwoDiceOutcome.SixSix
    }
