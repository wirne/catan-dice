package com.wirne.catandice.feature.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
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
import com.wirne.catandice.ui.theme.CDColor
import com.wirne.catandice.ui.theme.CDTheme

@Composable
fun GameScreen(
    openSettings: () -> Unit,
    openStats: () -> Unit,
    viewModel: GameViewModel = hiltViewModel(),
) {
    val (state, _, dispatch) = use(viewModel)

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
        ConstraintLayout(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            val (shipRef, dicesRef, rollButtonRef) = createRefs()

            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(),
                actions = {
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
                                tint = CDColor.White87,
                            )
                        }
                    }

                    IconButton(
                        onClick = openSettings,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = null,
                            tint = CDColor.White87,
                        )
                    }
                },
            )

            if (lastDiceRoll != null) {
                Dices(
                    modifier =
                        Modifier
                            .constrainAs(dicesRef) {
                                top.linkTo(parent.top)
                                if (state.citiesAndKnightsEnabled) {
                                    bottom.linkTo(shipRef.top)
                                } else {
                                    bottom.linkTo(parent.bottom)
                                }
                                centerHorizontallyTo(parent)
                            },
                    diceRoll = lastDiceRoll,
                    knightsAndCitiesEnabled = state.citiesAndKnightsEnabled,
                )
            }

            if (state.citiesAndKnightsEnabled) {
                Ship(
                    modifier =
                        Modifier
                            .constrainAs(shipRef) {
                                bottom.linkTo(parent.bottom)
                                centerHorizontallyTo(parent)
                            },
                    state = state.shipState,
                    onShipStateChange = {
                        dispatch(Event.OnShipStateChange(it))
                    },
                )
            }

            RollButton(
                modifier =
                    Modifier
                        .constrainAs(rollButtonRef) {
                            end.linkTo(parent.end)
                            centerVerticallyTo(parent)
                        },
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
