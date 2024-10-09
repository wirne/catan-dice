package com.wirne.catandice.feature.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.wirne.catandice.R
import com.wirne.catandice.ui.theme.*

@Composable
fun RandomPercentageSelector(
    percentage: Int,
    onPercentageSelected: (Int) -> Unit,
) {
    Column {
        var selectedPercentage by remember(percentage) { mutableFloatStateOf(percentage.toFloat()) }
        var showInfo: Boolean by remember { mutableStateOf(false) }

        if (showInfo) {
            Dialog(
                onDismissRequest = { showInfo = false },
            ) {
                Text(
                    modifier = Modifier
                        .clickable(onClick = { showInfo = false })
                        .background(CDColor.DarkGrey)
                        .padding(8.dp),
                    text =
                    """
                        With 0% random you'll end up with the expected distribution of the two dices every 36th turn. By adding randomness you'll have a chance to roll a completely random number. 100% random is the equivalent of using real dices.
                        
                        To get a better grasp of how it works, roll the dice a couple of times and look at the statistics. Then change randomness, roll again and compare.
                        """.trimIndent(),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = "Random percentage: ${selectedPercentage.toInt()}%")

            IconButton(
                onClick = { showInfo = true },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    tint = CDColor.White87,
                    contentDescription = "Info",
                )
            }
        }

        Slider(
            value = selectedPercentage,
            onValueChange = {
                selectedPercentage = it
            },
            onValueChangeFinished = {
                if (selectedPercentage.toInt() != percentage) {
                    onPercentageSelected(selectedPercentage.toInt())
                }
            },
            valueRange = 0f.rangeTo(100f),
            colors =
            SliderDefaults.colors(
                thumbColor = CDColor.Yellow,
                activeTrackColor = CDColor.Red,
            ),
        )
    }
}

@Composable
@Preview
private fun Preview() {
    CDTheme {
        Surface {
            RandomPercentageSelector(
                percentage = 10,
                onPercentageSelected = { },
            )
        }
    }
}
