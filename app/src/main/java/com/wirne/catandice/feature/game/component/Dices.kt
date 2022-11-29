package com.wirne.catandice.feature.game.component

import android.os.Build
import android.os.VibrationEffect
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import com.wirne.catandice.common.vibrator
import com.wirne.catandice.data.model.TwoDiceSum
import com.wirne.catandice.feature.game.DiceRoll

@Composable
fun Dices(
    modifier: Modifier,
    diceRoll: DiceRoll,
    knightsAndCitiesEnabled: Boolean
) {

    val rotation = remember { Animatable(0f) }
    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current
    var currentTurn: Int? by rememberSaveable {
        mutableStateOf(if (isPreview) diceRoll.turn else null)
    }

    LaunchedEffect(diceRoll.turn) {
        if (diceRoll.turn == currentTurn) return@LaunchedEffect
        if (isPreview) return@LaunchedEffect

        val effect = if (diceRoll.twoDiceOutcome.sum == TwoDiceSum.Seven) {
            VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK)
            } else {
                VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            }
        }
        context.vibrator.vibrate(effect)

        // We have to get the diff since animations can be cancelled
        // and we want the dice number to stand straight
        val diff = rotation.value.mod(360f)
        rotation.animateTo(
            targetValue = rotation.value + 360f - diff,
            animationSpec = tween(durationMillis = 300)
        ) {
            currentTurn = diceRoll.turn
        }
    }

    if (currentTurn == null) {
        return
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = knightsAndCitiesEnabled) {
            CitiesAndKnightsDice(
                rotation = rotation.value,
                knightsAndCitiesDiceOutcome = diceRoll.citiesAndKnightsDiceOutcome,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {

            NumberDice(
                rotation = rotation.value,
                outcome = diceRoll.twoDiceOutcome.redDice,
                diceColor = NumberDiceColors.Red
            )

            Spacer(modifier = Modifier.width(8.dp))

            NumberDice(
                rotation = rotation.value,
                outcome = diceRoll.twoDiceOutcome.yellowDice,
                diceColor = NumberDiceColors.Yellow
            )
        }
    }
}
