package com.wirne.catandice.feature.game

import androidx.compose.runtime.Immutable
import com.wirne.catandice.common.ViewModelContract

interface GameContract :
    ViewModelContract<GameContract.Event, GameContract.Effect, GameContract.State> {

    sealed class Effect

    sealed class Event {
        object Roll : Event()
        object Reset : Event()
    }

    @Immutable
    data class State(
        val diceRollHistory: List<DiceRoll>,
        val randomPercentage: Int,
        val citiesAndKnightsEnabled: Boolean,
    )
}
