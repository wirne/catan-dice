package com.wirne.catandice.data.model

import androidx.compose.runtime.Immutable

@Immutable
enum class TwoDiceOutcome(
    val redDice: DiceOutcome,
    val yellowDice: DiceOutcome,
    val sum: TwoDiceSum,
) {
    OneOne(DiceOutcome.One, DiceOutcome.One, TwoDiceSum.Two),

    TwoOne(DiceOutcome.Two, DiceOutcome.One, TwoDiceSum.Three),
    OneTwo(DiceOutcome.One, DiceOutcome.Two, TwoDiceSum.Three),

    ThreeOne(DiceOutcome.Three, DiceOutcome.One, TwoDiceSum.Four),
    TwoTwo(DiceOutcome.Two, DiceOutcome.Two, TwoDiceSum.Four),
    OneThree(DiceOutcome.One, DiceOutcome.Three, TwoDiceSum.Four),

    FourOne(DiceOutcome.Four, DiceOutcome.One, TwoDiceSum.Five),
    ThreeTwo(DiceOutcome.Three, DiceOutcome.Two, TwoDiceSum.Five),
    TwoThree(DiceOutcome.Two, DiceOutcome.Three, TwoDiceSum.Five),
    OneFour(DiceOutcome.One, DiceOutcome.Four, TwoDiceSum.Five),

    FiveOne(DiceOutcome.Five, DiceOutcome.One, TwoDiceSum.Six),
    FourTwo(DiceOutcome.Four, DiceOutcome.Two, TwoDiceSum.Six),
    ThreeThree(DiceOutcome.Three, DiceOutcome.Three, TwoDiceSum.Six),
    TwoFour(DiceOutcome.Two, DiceOutcome.Four, TwoDiceSum.Six),
    OneFive(DiceOutcome.One, DiceOutcome.Five, TwoDiceSum.Six),

    SixOne(DiceOutcome.Six, DiceOutcome.One, TwoDiceSum.Seven),
    FiveTwo(DiceOutcome.Five, DiceOutcome.Two, TwoDiceSum.Seven),
    FourThree(DiceOutcome.Four, DiceOutcome.Three, TwoDiceSum.Seven),
    ThreeFour(DiceOutcome.Three, DiceOutcome.Four, TwoDiceSum.Seven),
    TwoFive(DiceOutcome.Two, DiceOutcome.Five, TwoDiceSum.Seven),
    OneSix(DiceOutcome.One, DiceOutcome.Six, TwoDiceSum.Seven),

    SixTwo(DiceOutcome.Six, DiceOutcome.Two, TwoDiceSum.Eight),
    FiveThree(DiceOutcome.Five, DiceOutcome.Three, TwoDiceSum.Eight),
    FourFour(DiceOutcome.Four, DiceOutcome.Four, TwoDiceSum.Eight),
    ThreeFive(DiceOutcome.Three, DiceOutcome.Five, TwoDiceSum.Eight),
    TwoSix(DiceOutcome.Two, DiceOutcome.Six, TwoDiceSum.Eight),

    SixThree(DiceOutcome.Six, DiceOutcome.Three, TwoDiceSum.Nine),
    FiveFour(DiceOutcome.Five, DiceOutcome.Four, TwoDiceSum.Nine),
    FourFive(DiceOutcome.Four, DiceOutcome.Five, TwoDiceSum.Nine),
    ThreeSix(DiceOutcome.Three, DiceOutcome.Six, TwoDiceSum.Nine),

    SixFour(DiceOutcome.Six, DiceOutcome.Four, TwoDiceSum.Ten),
    FiveFive(DiceOutcome.Five, DiceOutcome.Five, TwoDiceSum.Ten),
    FourSix(DiceOutcome.Four, DiceOutcome.Six, TwoDiceSum.Ten),

    SixFive(DiceOutcome.Six, DiceOutcome.Five, TwoDiceSum.Eleven),
    FiveSix(DiceOutcome.Five, DiceOutcome.Six, TwoDiceSum.Eleven),

    SixSix(DiceOutcome.Six, DiceOutcome.Six, TwoDiceSum.Twelve),
}
