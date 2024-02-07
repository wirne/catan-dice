package com.wirne.catandice.feature.game.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.DiceDots(
    number: Number,
    color: Color,
) {
    when (number) {
        1 -> DiceDotsOne(color = color)
        2 -> DiceDotsTwo(color = color)
        3 -> DiceDotsThree(color = color)
        4 -> DiceDotsFour(color = color)
        5 -> DiceDotsFive(color = color)
        6 -> DiceDotsSix(color = color)
    }
}

@Composable
private fun BoxScope.Dot(
    alignment: Alignment,
    color: Color,
) {
    Box(
        modifier =
            Modifier
                .align(alignment)
                .size(16.dp)
                .background(
                    color = color,
                    shape = CircleShape,
                ),
    )
}

@Composable
private fun BoxScope.DiceDotsOne(color: Color) {
    Dot(
        alignment = Alignment.Center,
        color = color,
    )
}

@Composable
private fun BoxScope.DiceDotsTwo(color: Color) {
    Dot(
        alignment = Alignment.TopStart,
        color = color,
    )
    Dot(
        alignment = Alignment.BottomEnd,
        color = color,
    )
}

@Composable
private fun BoxScope.DiceDotsThree(color: Color) {
    Dot(
        alignment = Alignment.TopStart,
        color = color,
    )
    Dot(
        alignment = Alignment.Center,
        color = color,
    )
    Dot(
        alignment = Alignment.BottomEnd,
        color = color,
    )
}

@Composable
private fun BoxScope.DiceDotsFour(color: Color) {
    Dot(
        alignment = Alignment.TopStart,
        color = color,
    )
    Dot(
        alignment = Alignment.BottomStart,
        color = color,
    )
    Dot(
        alignment = Alignment.TopEnd,
        color = color,
    )
    Dot(
        alignment = Alignment.BottomEnd,
        color = color,
    )
}

@Composable
private fun BoxScope.DiceDotsFive(color: Color) {
    Dot(
        alignment = Alignment.TopStart,
        color = color,
    )
    Dot(
        alignment = Alignment.BottomStart,
        color = color,
    )
    Dot(
        alignment = Alignment.Center,
        color = color,
    )
    Dot(
        alignment = Alignment.TopEnd,
        color = color,
    )
    Dot(
        alignment = Alignment.BottomEnd,
        color = color,
    )
}

@Composable
private fun BoxScope.DiceDotsSix(color: Color) {
    Dot(
        alignment = Alignment.TopStart,
        color = color,
    )
    Dot(
        alignment = Alignment.CenterStart,
        color = color,
    )
    Dot(
        alignment = Alignment.BottomStart,
        color = color,
    )
    Dot(
        alignment = Alignment.TopEnd,
        color = color,
    )
    Dot(
        alignment = Alignment.CenterEnd,
        color = color,
    )
    Dot(
        alignment = Alignment.BottomEnd,
        color = color,
    )
}
