package com.wirne.catandice.feature.game.component

import android.os.VibrationEffect
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
    val Width = 350.dp
    val ShipSize = 48.dp
    val AnchorSize = 8.dp
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Ship(
    modifier: Modifier,
    state: ShipState,
    onShipStateChange: (ShipState) -> Unit
) {
    val context = LocalContext.current
    val stepWidthPx: Float = with(LocalDensity.current) {
        ShipDefaults.Width.toPx() / ShipState.entries.count()
    }
    val halfShipSizePx = with(LocalDensity.current) {
        (ShipDefaults.ShipSize.toPx() / 2).roundToInt()
    }
    val halfAnchorSizePx = with(LocalDensity.current) {
        (ShipDefaults.AnchorSize.toPx() / 2).roundToInt()
    }

    val anchors = ShipState.entries
        .mapIndexed { index, shipState ->
            shipState to (index * stepWidthPx)
        }

    val draggableAnchors = DraggableAnchors {
        for ((anchor, position) in anchors) {
            anchor at position
        }
    }

    val anchorDraggableState = remember {
        AnchoredDraggableState(
            initialValue = state,
            positionalThreshold = { distance -> distance * 0.5f },
            velocityThreshold = { Float.MAX_VALUE },
            animationSpec = spring(),
            anchors = draggableAnchors,
            confirmValueChange = {
                onShipStateChange(it)
                true
            }
        )
    }

    LaunchedEffect(state) {
        if (state != anchorDraggableState.currentValue) {
            anchorDraggableState.animateTo(state)
        }

        if (state == anchorDraggableState.currentValue && state == ShipState.Eight) {
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

        anchors.forEach { (shipState, offset) ->
            val padding = if (shipState == ShipState.Eight) 4.dp else 0.dp
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
                .offset { IntOffset(anchorDraggableState.offset.roundToIntOrZero(), 0) }
                .anchoredDraggable(
                    state = anchorDraggableState,
                    orientation = Orientation.Horizontal
                )
                .size(ShipDefaults.ShipSize),
            painter = painterResource(id = R.drawable.ic_ship),
            tint = Color.White,
            contentDescription = null
        )
    }
}

private fun Float.roundToIntOrZero(): Int = try {
    roundToInt()
} catch (e: Exception) {
    0
}
