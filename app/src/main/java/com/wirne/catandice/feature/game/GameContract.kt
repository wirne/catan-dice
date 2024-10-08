package com.wirne.catandice.feature.game

import androidx.compose.runtime.Immutable
import com.wirne.catandice.common.ViewModelContract
import com.wirne.catandice.data.model.ShipState

interface GameContract : ViewModelContract<GameContract.Event, GameContract.State> {

    sealed class Event {
        data object Roll : Event()

        data object Reset : Event()

        data class OnShipStateChange(val state: ShipState) : Event()
    }

    @Immutable
    data class State(
        val diceRollHistory: List<DiceRoll>,
        val randomPercentage: Int,
        val citiesAndKnightsEnabled: Boolean,
        val shipState: ShipState,
    )
}
