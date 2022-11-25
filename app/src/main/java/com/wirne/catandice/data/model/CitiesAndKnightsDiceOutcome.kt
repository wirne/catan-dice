package com.wirne.catandice.data.model

import androidx.compose.runtime.Immutable

@Immutable
enum class CitiesAndKnightsDiceOutcome {
    Ship1,
    Ship2,
    Ship3,
    Blue,
    Yellow,
    Green;

    fun isShip(): Boolean = when (this) {
        Ship1,
        Ship2,
        Ship3 -> true

        Blue -> false
        Yellow -> false
        Green -> false
    }
}
