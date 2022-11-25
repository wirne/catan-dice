package com.wirne.catandice.data.model

import androidx.compose.runtime.Immutable

@Immutable
enum class DiceOutcome(val number: Int) {
    One(1),
    Two(2),
    Three(3),
    Four(4),
    Five(5),
    Six(6)
}
