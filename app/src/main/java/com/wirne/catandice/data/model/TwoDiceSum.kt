package com.wirne.catandice.data.model

enum class TwoDiceSum(
    val sum: Int,
    val chance: Float,
) {
    Two(2, 1 / 36f),
    Three(3, 2 / 36f),
    Four(4, 3 / 36f),
    Five(5, 4 / 36f),
    Six(6, 5 / 36f),
    Seven(7, 6 / 36f),
    Eight(8, 5 / 36f),
    Nine(9, 4 / 36f),
    Ten(10, 3 / 36f),
    Eleven(11, 2 / 36f),
    Twelve(12, 1 / 36f),
}
