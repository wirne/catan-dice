package com.wirne.catandice.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wirne.catandice.R
import com.wirne.catandice.common.use
import com.wirne.catandice.feature.settings.SettingsContract.Event
import com.wirne.catandice.feature.settings.SettingsContract.State
import com.wirne.catandice.feature.settings.component.RandomPercentageSelector
import com.wirne.catandice.feature.settings.component.SwitchRow
import com.wirne.catandice.feature.settings.component.TimerSelector
import com.wirne.catandice.ui.theme.CDTheme
import kotlin.time.Duration.Companion.minutes

@Composable
fun SettingsScreen(
    onNavigationIconClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val (state, _, dispatch) = use(viewModel)
    SettingsScreen(
        onNavigationIconClick = onNavigationIconClick,
        state = state,
        dispatch = dispatch,
    )
}

@Composable
private fun SettingsScreen(
    onNavigationIconClick: () -> Unit,
    state: State,
    dispatch: (Event) -> Unit,
) {
    Surface {
        Box(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            Column(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 16.dp)
                        .requiredWidthIn(max = 400.dp),
            ) {
                RandomPercentageSelector(
                    percentage = state.randomPercentage,
                    onPercentageSelected = { percentage ->
                        dispatch(Event.OnPercentageSelected(percentage))
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                TimerSelector(
                    time = state.time,
                    onTimeSelected = { time ->
                        dispatch(Event.OnTimeSelected(time))
                    },
                )

                SwitchRow(
                    label = "Enable timer",
                    checked = state.timerEnabled,
                    onCheckedChange = {
                        dispatch(Event.OnTimerEnabledClicked)
                    },
                )

                SwitchRow(
                    label = "Enable Cities and Knights",
                    checked = state.citiesAndKnightsEnabled,
                    onCheckedChange = {
                        dispatch(Event.OnCitiesAndKnightsEnabledClicked)
                    },
                )
            }

            IconButton(
                modifier =
                    Modifier
                        .padding(4.dp)
                        .align(Alignment.TopStart),
                onClick = onNavigationIconClick,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
@Preview(name = "mobile", widthDp = 891, heightDp = 411, device = Devices.AUTOMOTIVE_1024p)
@Preview(name = "mobileSmall", widthDp = 640, heightDp = 360, device = Devices.AUTOMOTIVE_1024p)
private fun Preview() {
    CDTheme {
        SettingsScreen(
            state =
                State(
                    randomPercentage = 10,
                    citiesAndKnightsEnabled = true,
                    time = 10.minutes,
                    timerEnabled = true,
                ),
            dispatch = { },
            onNavigationIconClick = { },
        )
    }
}
