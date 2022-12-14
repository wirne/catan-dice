package com.wirne.catandice.feature.game.component

import android.os.VibrationEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.wirne.catandice.R
import com.wirne.catandice.common.vibrator
import com.wirne.catandice.data.model.ShipState
import com.wirne.catandice.ui.theme.CDColor
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

object ShipDefaults {
    val Width = 300.dp
    val ShipSize = 48.dp
    val AnchorSize = 8.dp
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Ship(
    modifier: Modifier,
    state: ShipState,
    onShipStateChange: (ShipState) -> Unit
) {
    val context = LocalContext.current
    val stepWidthPx: Float = with(LocalDensity.current) {
        ShipDefaults.Width.toPx() / ShipState.values().count()
    }
    val halfShipSizePx = with(LocalDensity.current) {
        (ShipDefaults.ShipSize.toPx() / 2).roundToInt()
    }
    val halfAnchorSizePx = with(LocalDensity.current) {
        (ShipDefaults.AnchorSize.toPx() / 2).roundToInt()
    }

    val anchors = ShipState.values()
        .mapIndexed { index, shipState ->
            (index * stepWidthPx) to shipState
        }
        .toMap()

    val swipeableState = rememberSwipeableState(
        initialValue = state,
        confirmStateChange = {
            onShipStateChange(it)
            true
        }
    )

    LaunchedEffect(state) {
        if (state != swipeableState.currentValue) {
            swipeableState.animateTo(state)
        }

        if (state == swipeableState.currentValue && state == ShipState.Seven) {
            context.vibrator.vibrate(
                VibrationEffect.createOneShot(
                    500,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
            delay(500)
            onShipStateChange(ShipState.One)
        }
    }

    Box(
        modifier = modifier
            .width(ShipDefaults.Width),
        contentAlignment = Alignment.CenterStart
    ) {

        Box(
            modifier = Modifier
                .width(ShipDefaults.Width - ShipDefaults.ShipSize)
                .align(Alignment.Center)
                .height(2.dp)
                .background(CDColor.Red)
        )

        anchors.forEach { (offset, shipState) ->
            val padding = if (shipState == ShipState.Seven) 4.dp else 0.dp
            val halfPaddingPx = with(LocalDensity.current) {
                (padding.toPx() / 2).roundToInt()
            }

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            offset.roundToInt() + halfShipSizePx - halfAnchorSizePx - halfPaddingPx,
                            0
                        )
                    }
                    .background(CDColor.DarkRed, CircleShape)
                    .padding(padding)
                    .size(ShipDefaults.AnchorSize)
                    .background(CDColor.Yellow, CircleShape)
            )
        }

        Icon(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )
                .size(ShipDefaults.ShipSize),
            painter = painterResource(id = R.drawable.ic_ship),
            tint = Color.White,
            contentDescription = null
        )
    }
}
