package com.wirne.catandice.feature.game.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.wirne.catandice.ui.theme.CDColor

@Composable
fun RollButton(
    modifier: Modifier,
    onClick: () -> Unit,
) = Text(
    modifier = modifier
        .padding(16.dp)
        .size(80.dp)
        .background(
            shape = CircleShape,
            color = CDColor.Grey,
        )
        .clip(CircleShape)
        .clickable(onClick = onClick)
        .wrapContentSize(Alignment.Center),
    text = "Roll",
    style = with(LocalDensity.current) {
        MaterialTheme.typography.headlineSmall.copy(fontSize = 24.dp.toSp())
    },
)
