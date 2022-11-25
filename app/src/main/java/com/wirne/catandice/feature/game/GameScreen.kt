package com.wirne.catandice.feature.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wirne.catandice.R
import com.wirne.catandice.common.use
import com.wirne.catandice.data.model.CitiesAndKnightsDiceOutcome
import com.wirne.catandice.data.model.TwoDiceOutcome
import com.wirne.catandice.feature.game.GameContract.Event
import com.wirne.catandice.feature.game.GameContract.State
import com.wirne.catandice.feature.game.component.Dices
import com.wirne.catandice.feature.game.component.ResetButton
import com.wirne.catandice.ui.theme.CDColor
import com.wirne.catandice.ui.theme.CDTheme

@Composable
fun GameRoute(
    openSettings: () -> Unit,
    openStats: () -> Unit,
    viewModel: GameViewModel = hiltViewModel()
) {

    val (state, _, dispatch) = use(viewModel)

    GameScreen(
        state = state,
        dispatch = dispatch,
        openStats = openStats,
        openSettings = openSettings
    )
}

@Composable
private fun GameScreen(
    state: State,
    dispatch: (Event) -> Unit,
    openSettings: () -> Unit,
    openStats: () -> Unit,
) {
    val lastDiceRoll = state.diceRollHistory.lastOrNull()

    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnimatedVisibility(visible = state.diceRollHistory.isNotEmpty()) {
                    ResetButton(
                        reset = { dispatch(Event.Reset) }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                if (state.diceRollHistory.isNotEmpty()) {
                    IconButton(
                        onClick = openStats
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bar_chart),
                            contentDescription = null,
                            tint = CDColor.White87
                        )
                    }
                }

                IconButton(
                    onClick = openSettings
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = null,
                        tint = CDColor.White87
                    )
                }
            }

            if (lastDiceRoll != null) {
                Dices(
                    diceRoll = lastDiceRoll,
                    knightsAndCitiesEnabled = state.citiesAndKnightsEnabled
                )
            }

            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .size(80.dp)
                    .align(Alignment.CenterEnd),
                shape = CircleShape,
                onClick = {
                    dispatch(Event.Roll)
                }
            ) {
                Text(
                    text = "Roll",
                    style = with(LocalDensity.current) {
                        MaterialTheme.typography.h5.copy(fontSize = 24.dp.toSp())
                    }
                )
            }
        }
    }
}

@Composable
@Preview(name = "mobile", widthDp = 891, heightDp = 411)
@Preview(name = "tablet", device = Devices.NEXUS_6)
@Preview(name = "desktop", widthDp = (16 * 500), heightDp = (9 * 500))
private fun Preview() {
    CDTheme {
        GameScreen(
            state = State(
                diceRollHistory = listOf(
                    DiceRoll(
                        twoDiceOutcome = TwoDiceOutcome.FiveTwo,
                        citiesAndKnightsDiceOutcome = CitiesAndKnightsDiceOutcome.Green,
                        turn = 4,
                        random = false
                    )
                ),
                randomPercentage = 10,
                citiesAndKnightsEnabled = true,
            ),
            dispatch = { },
            openSettings = { },
            openStats = { }
        )
    }
}

@Composable
@Preview
private fun PreviewEmpty() {
    CDTheme {
        GameScreen(
            state = State(
                diceRollHistory = emptyList(),
                randomPercentage = 10,
                citiesAndKnightsEnabled = true,
            ),
            dispatch = { },
            openSettings = { },
            openStats = { }
        )
    }
}
