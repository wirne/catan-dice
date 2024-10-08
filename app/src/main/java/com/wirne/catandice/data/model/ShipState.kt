package com.wirne.catandice.data.model

enum class ShipState {
    One,
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight;

    fun next(): ShipState = when (this) {
        One -> Two
        Two -> Three
        Three -> Four
        Four -> Five
        Five -> Six
        Six -> Seven
        Seven -> Eight
        Eight -> One
    }
}
