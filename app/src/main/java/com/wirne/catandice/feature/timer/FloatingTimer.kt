package com.wirne.catandice.feature.timer

import android.os.VibrationEffect
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wirne.catandice.R
import com.wirne.catandice.common.use
import com.wirne.catandice.common.vibrator
import com.wirne.catandice.feature.timer.FloatingTimerContract.Event
import com.wirne.catandice.feature.timer.FloatingTimerContract.State
import com.wirne.catandice.ui.theme.CDColor
import com.wirne.catandice.ui.theme.CDTheme
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds

private const val MAX_AMPLITUDE = 255

@Composable
fun FloatingTimer(
    modifier: Modifier = Modifier,
    viewModel: FloatingTimerViewModel = hiltViewModel()
) {
    val (state, dispatch) = use(viewModel)

    if (state.enabled && state.gotHistory) {
        FloatingTimerImpl(
            modifier = modifier,
            state = state,
            dispatch = dispatch,
        )
    }
}

@Composable
private fun FloatingTimerImpl(
    modifier: Modifier = Modifier,
    state: State,
    dispatch: (Event) -> Unit,
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val context = LocalContext.current

    LaunchedEffect(key1 = state.shouldVibrate) {
        if (state.shouldVibrate) {
            context.vibrator.vibrate(
                VibrationEffect.createOneShot(
                    2_000,
                    MAX_AMPLITUDE,
                ),
            )

            dispatch(Event.OnVibrate)
        }
    }

    Surface(
        modifier = modifier
            .safeContentPadding()
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            },
        border = BorderStroke(1.dp, CDColor.White40),
        color = CDColor.Grey,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                text = state.timeLeft.toString(),
                color = if (state.timeLeft < 30.seconds) Color.Red else CDColor.White85,
            )

            Row {
                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = { dispatch(Event.OnPlayTimer) },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "Play",
                    )
                }

                IconButton(
                    onClick = { dispatch(Event.OnPauseTimer) },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pause),
                        contentDescription = "Pause",
                    )
                }

                IconButton(
                    onClick = { dispatch(Event.OnStopTimer) },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_stop),
                        contentDescription = "Stop",
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    CDTheme {
        FloatingTimerImpl(
            state =
            State(
                timeLeft = 100.seconds,
                enabled = true,
                running = true,
                gotHistory = true,
                shouldVibrate = false,
            ),
            dispatch = { },
        )
    }
}
