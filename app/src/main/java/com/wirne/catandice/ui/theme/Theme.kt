package com.wirne.catandice.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
    primary = CDColor.Red,
    onPrimary = CDColor.Yellow,
    secondary = CDColor.Yellow,
    onSecondary = CDColor.Red,
    surface = CDColor.DarkGrey,
    background = CDColor.DarkGrey,
    onSurface = CDColor.White85,
    onBackground = CDColor.White85,
    surfaceVariant = CDColor.Grey,
    onSurfaceVariant = CDColor.White85,
)

@Composable
fun CDTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorPalette,
        content = content,
    )
}
