package com.wirne.catandice

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.wirne.catandice.ui.theme.CDTheme

@Composable
private fun DiceImage() {
    val (width, height) = with(LocalDensity.current) {
        7000.toDp() to 5000.toDp()
    }

    Image(
        modifier = Modifier
            .size(width, height),
        painter = painterResource(id = R.drawable.ic_dices),
        contentDescription = null,
    )
}

@Preview
@Composable
private fun Preview() {
    CDTheme {
        Surface {
            DiceImage()
        }
    }
}
