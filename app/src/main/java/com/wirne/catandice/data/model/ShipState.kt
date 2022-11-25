package com.wirne.catandice.data.model

enum class ShipState {
    One,
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven;

    fun next(): ShipState = when (this) {
        One -> Two
        Two -> Three
        Three -> Four
        Four -> Five
        Five -> Six
        Six -> Seven
        Seven -> One
    }
}
