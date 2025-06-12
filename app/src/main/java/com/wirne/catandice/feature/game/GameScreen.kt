package com.wirne.catandice.feature.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wirne.catandice.R
import com.wirne.catandice.common.use
import com.wirne.catandice.data.model.CitiesAndKnightsDiceOutcome
import com.wirne.catandice.data.model.ShipState
import com.wirne.catandice.data.model.TwoDiceOutcome
import com.wirne.catandice.feature.game.GameContract.Event
import com.wirne.catandice.feature.game.GameContract.State
import com.wirne.catandice.feature.game.component.Dices
import com.wirne.catandice.feature.game.component.ResetButton
import com.wirne.catandice.feature.game.component.RollButton
import com.wirne.catandice.feature.game.component.Ship
import com.wirne.catandice.ui.theme.CDTheme

@Composable
fun GameScreen(
    openSettings: () -> Unit,
    openStats: () -> Unit,
    viewModel: GameViewModel = hiltViewModel(),
) {
    val (state, dispatch) = use(viewModel)

    GameScreen(
        state = state,
        dispatch = dispatch,
        openStats = openStats,
        openSettings = openSettings,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
                .safeContentPadding()
                .fillMaxSize(),
        ) {

            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
                windowInsets = WindowInsets(0),
                actions = {
                    Spacer(modifier = Modifier.width(8.dp))

                    AnimatedVisibility(
                        visible = state.diceRollHistory.isNotEmpty(),
                    ) {
                        ResetButton(
                            reset = { dispatch(Event.Reset) },
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    AnimatedVisibility(
                        visible = state.diceRollHistory.isNotEmpty(),
                    ) {
                        IconButton(
                            onClick = openStats,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bar_chart),
                                contentDescription = null,
                            )
                        }
                    }

                    IconButton(
                        onClick = openSettings,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = null,
                        )
                    }
                },
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {

                if (lastDiceRoll != null) {
                    Dices(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentSize(align = Alignment.Center),
                        diceRoll = lastDiceRoll,
                        knightsAndCitiesEnabled = state.citiesAndKnightsEnabled,
                    )
                }

                if (state.citiesAndKnightsEnabled) {
                    Ship(
                        modifier = Modifier,
                        state = state.shipState,
                        onShipStateChange = {
                            dispatch(Event.OnShipStateChange(it))
                        },
                    )
                }
            }

            RollButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    dispatch(Event.Roll)
                },
            )
        }
    }
}

@Composable
@Preview(name = "mobile", widthDp = 891, heightDp = 411)
@Preview(name = "tablet", device = Devices.NEXUS_6)
private fun Preview() {
    CDTheme {
        GameScreen(
            state =
                State(
                    diceRollHistory =
                        listOf(
                            DiceRoll(
                                twoDiceOutcome = TwoDiceOutcome.FiveTwo,
                                citiesAndKnightsDiceOutcome = CitiesAndKnightsDiceOutcome.Green,
                                turn = 4,
                                random = false,
                            ),
                        ),
                    randomPercentage = 10,
                    citiesAndKnightsEnabled = true,
                    shipState = ShipState.Five,
                ),
            dispatch = { },
            openSettings = { },
            openStats = { },
        )
    }
}

@Composable
@Preview
private fun PreviewEmpty() {
    CDTheme {
        GameScreen(
            state =
                State(
                    diceRollHistory = emptyList(),
                    randomPercentage = 10,
                    citiesAndKnightsEnabled = true,
                    shipState = ShipState.Five,
                ),
            dispatch = { },
            openSettings = { },
            openStats = { },
        )
    }
}
