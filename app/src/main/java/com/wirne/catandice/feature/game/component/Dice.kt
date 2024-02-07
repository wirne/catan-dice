package com.wirne.catandice.feature.game.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wirne.catandice.R
import com.wirne.catandice.data.model.CitiesAndKnightsDiceOutcome
import com.wirne.catandice.data.model.DiceOutcome
import com.wirne.catandice.ui.theme.*

@Immutable
enum class NumberDiceColors(
    val background: Color,
    val dot: Color,
) {
    Red(
        background = CDColor.Red,
        dot = CDColor.Yellow,
    ),
    Yellow(
        background = CDColor.Yellow,
        dot = CDColor.Red,
    ),
}

@Composable
fun CitiesAndKnightsDice(
    rotation: Float,
    knightsAndCitiesDiceOutcome: CitiesAndKnightsDiceOutcome,
) {
    CustomDice(
        background = Color.White,
        rotation = rotation,
    ) {
        val (drawableRes, color) =
            when (knightsAndCitiesDiceOutcome) {
                CitiesAndKnightsDiceOutcome.Ship1,
                CitiesAndKnightsDiceOutcome.Ship2,
                CitiesAndKnightsDiceOutcome.Ship3,
                -> (R.drawable.ic_ship to Color.Black)

                CitiesAndKnightsDiceOutcome.Blue -> (R.drawable.ic_castle to CDColor.Blue)
                CitiesAndKnightsDiceOutcome.Yellow -> (R.drawable.ic_castle to CDColor.LightYellow)
                CitiesAndKnightsDiceOutcome.Green -> (R.drawable.ic_castle to CDColor.Green)
            }

        Icon(
            modifier =
                Modifier
                    .size(60.dp),
            painter = painterResource(id = drawableRes),
            tint = color,
            contentDescription = null,
        )
    }
}

@Composable
fun NumberDice(
    rotation: Float,
    outcome: DiceOutcome,
    diceColor: NumberDiceColors,
) {
    CustomDice(
        background = diceColor.background,
        rotation = rotation,
    ) {
        DiceDots(
            number = outcome.number,
            color = diceColor.dot,
        )
    }
}

@Composable
private fun CustomDice(
    background: Color,
    rotation: Float,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier =
            Modifier
                .size(80.dp)
                .rotate(rotation)
                .background(
                    color = background,
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(12.dp),
        contentAlignment = Alignment.Center,
        content = content,
    )
}

@Composable
@Preview
private fun PreviewNumberDice() {
    CDTheme {
        Surface {
            Column {
                NumberDice(
                    outcome = DiceOutcome.One,
                    diceColor = NumberDiceColors.Red,
                    rotation = 0f,
                )
                NumberDice(
                    outcome = DiceOutcome.Two,
                    diceColor = NumberDiceColors.Yellow,
                    rotation = 0f,
                )
                NumberDice(
                    outcome = DiceOutcome.Three,
                    diceColor = NumberDiceColors.Red,
                    rotation = 0f,
                )
                NumberDice(
                    outcome = DiceOutcome.Four,
                    diceColor = NumberDiceColors.Yellow,
                    rotation = 0f,
                )
                NumberDice(
                    outcome = DiceOutcome.Five,
                    diceColor = NumberDiceColors.Red,
                    rotation = 0f,
                )
                NumberDice(
                    outcome = DiceOutcome.Six,
                    diceColor = NumberDiceColors.Yellow,
                    rotation = 0f,
                )
            }
        }
    }
}

@Composable
@Preview
private fun PreviewCitiesAndKnights() {
    CDTheme {
        Surface {
            Column {
                CitiesAndKnightsDice(
                    rotation = 0f,
                    knightsAndCitiesDiceOutcome = CitiesAndKnightsDiceOutcome.Green,
                )
                CitiesAndKnightsDice(
                    rotation = 0f,
                    knightsAndCitiesDiceOutcome = CitiesAndKnightsDiceOutcome.Blue,
                )
                CitiesAndKnightsDice(
                    rotation = 0f,
                    knightsAndCitiesDiceOutcome = CitiesAndKnightsDiceOutcome.Yellow,
                )
                CitiesAndKnightsDice(
                    rotation = 0f,
                    knightsAndCitiesDiceOutcome = CitiesAndKnightsDiceOutcome.Ship1,
                )
            }
        }
    }
}
