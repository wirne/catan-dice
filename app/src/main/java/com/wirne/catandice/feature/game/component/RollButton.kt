package com.wirne.catandice.feature.game.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun RollButton(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .padding(16.dp)
            .size(80.dp),
        shape = CircleShape,
        onClick = onClick
    ) {
        Text(
            text = "Roll",
            style = with(LocalDensity.current) {
                MaterialTheme.typography.h5.copy(fontSize = 24.dp.toSp())
            }
        )
    }
}
