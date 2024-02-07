package com.wirne.catandice.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette =
    darkColorScheme(
        primary = CDColor.White87,
        // primaryVariant = Color.Black,
        surface = CDColor.DarkGrey,
        secondary = CDColor.White87,
    )

@Composable
fun CDTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorPalette,
        content = content,
    )
}
