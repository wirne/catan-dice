package com.wirne.catandice.feature.timer

import android.os.VibrationEffect
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.wirne.catandice.feature.timer.FloatingTimerContract.State
import com.wirne.catandice.feature.timer.FloatingTimerContract.Event
import com.wirne.catandice.feature.timer.FloatingTimerContract.Effect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wirne.catandice.R
import com.wirne.catandice.common.collectInLaunchedEffect
import com.wirne.catandice.common.use
import com.wirne.catandice.common.vibrator
import com.wirne.catandice.ui.theme.CDColor
import com.wirne.catandice.ui.theme.CDTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds

private const val MAX_AMPLITUDE = 255

@Composable
fun FloatingTimer(
    viewModel: FloatingTimerViewModel = hiltViewModel()
) {

    val (state, effectFlow, dispatch) = use(viewModel)

    if (state.enabled && state.gotHistory) {
        FloatingTimerImpl(
            state = state,
            dispatch = dispatch,
            effectFlow = effectFlow
        )
    }
}

@Composable
private fun FloatingTimerImpl(
    state: State,
    dispatch: (Event) -> Unit,
    effectFlow: Flow<Effect>
) {

    val isPreview = LocalInspectionMode.current
    var offsetX by remember { mutableStateOf(if (isPreview) 0f else 50f) }
    var offsetY by remember { mutableStateOf(if (isPreview) 0f else 200f) }
    val context = LocalContext.current

    effectFlow.collectInLaunchedEffect { effect ->
        when (effect) {
            Effect.Timeout -> {
                context.vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        2_000,
                        MAX_AMPLITUDE
                    )
                )
            }
        }
    }

    Surface(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            },
        border = BorderStroke(1.dp, CDColor.White40),
        color = CDColor.Grey
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                text = state.timeLeft.toString(),
                color = if (state.timeLeft < 30.seconds) Color.Red else CDColor.White87
            )

            Row {

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = { dispatch(Event.OnPlayTimer) }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = "Play",
                        tint = CDColor.White87
                    )
                }

                IconButton(
                    onClick = { dispatch(Event.OnPauseTimer) }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pause),
                        contentDescription = "Pause",
                        tint = CDColor.White87
                    )
                }

                IconButton(
                    onClick = { dispatch(Event.OnStopTimer) }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_stop),
                        contentDescription = "Stop",
                        tint = CDColor.White87
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
            state = State(
                timeLeft = 100.seconds,
                enabled = true,
                running = true,
                gotHistory = true
            ),
            dispatch = { },
            effectFlow = emptyFlow()
        )
    }
}
