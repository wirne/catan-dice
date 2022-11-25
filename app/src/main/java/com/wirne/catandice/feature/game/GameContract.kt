package com.wirne.catandice.feature.game

import androidx.compose.runtime.Immutable
import com.wirne.catandice.common.ViewModelContract
import com.wirne.catandice.data.model.ShipState

interface GameContract :
    ViewModelContract<GameContract.Event, GameContract.Effect, GameContract.State> {

    sealed class Effect

    sealed class Event {
        object Roll : Event()
        object Reset : Event()
        data class OnShipStateChange(val state: ShipState) : Event()
    }

    @Immutable
    data class State(
        val diceRollHistory: List<DiceRoll>,
        val randomPercentage: Int,
        val citiesAndKnightsEnabled: Boolean,
        val shipState: ShipState
    )
}
