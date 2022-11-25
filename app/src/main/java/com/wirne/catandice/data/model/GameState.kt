package com.wirne.catandice.data.model

import com.wirne.catandice.feature.game.DiceRoll

data class GameState(
    val rollHistory: List<DiceRoll>,
    val twoDiceEntropy: List<TwoDiceOutcome>,
    val shipState: ShipState
)
