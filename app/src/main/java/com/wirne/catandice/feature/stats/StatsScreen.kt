package com.wirne.catandice.feature.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.wirne.catandice.R
import com.wirne.catandice.common.use
import com.wirne.catandice.data.model.TwoDiceSum
import com.wirne.catandice.feature.stats.StatsContract.State
import com.wirne.catandice.ui.theme.*
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun StatsScreen(
    onNavigationIconClick: () -> Unit,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val (state, _, _) = use(viewModel)
    StatsScreen(
        onNavigationIconClick = onNavigationIconClick,
        state = state,
    )
}

@Composable
private fun StatsScreen(
    onNavigationIconClick: () -> Unit,
    state: State
) {

    Surface {

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {

            val (xLabelRef, xValuesRef, yLabelRef, graphRef, infoBoxRef) = createRefs()

            IconButton(
                modifier = Modifier
                    .padding(4.dp),
                onClick = onNavigationIconClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }

            YLabel(
                modifier = Modifier.constrainAs(yLabelRef) {
                    centerVerticallyTo(graphRef)
                    end.linkTo(graphRef.start)
                }
            )

            Graph(
                modifier = Modifier.constrainAs(graphRef) {
                    height = Dimension.fillToConstraints
                    top.linkTo(parent.top, margin = 8.dp)
                    bottom.linkTo(xValuesRef.top)
                    centerHorizontallyTo(parent)
                },
                state = state
            )

            XValues(
                modifier = Modifier
                    .constrainAs(xValuesRef) {
                        centerHorizontallyTo(graphRef)
                        top.linkTo(graphRef.bottom)
                        bottom.linkTo(xLabelRef.top)
                    }
            )

            XLabel(
                modifier = Modifier
                    .constrainAs(xLabelRef) {
                        centerHorizontallyTo(graphRef)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                    }
            )

            InfoBox(
                modifier = Modifier
                    .constrainAs(infoBoxRef) {
                        top.linkTo(parent.top, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                    },
                turns = state.turns
            )
        }
    }
}

@Composable
private fun XValues(
    modifier: Modifier
) {
    Row(
        modifier = modifier
    ) {
        for (twoDiceSum in TwoDiceSum.values()) {
            Text(
                modifier = Modifier
                    .width(BarDefaults.WidthWithPadding),
                textAlign = TextAlign.Center,
                text = twoDiceSum.sum.toString()
            )
        }
    }
}

@Composable
private fun YLabel(
    modifier: Modifier
) {
    Text(
        modifier = modifier
            .rotate(270f),
        textAlign = TextAlign.Center,
        text = "Roll count"
    )
}

@Composable
private fun XLabel(
    modifier: Modifier
) {
    Text(
        textAlign = TextAlign.Center,
        modifier = modifier,
        text = "Dice sum"
    )
}

@Composable
private fun InfoBox(
    modifier: Modifier,
    turns: Int
) {

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Column(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .padding(8.dp)
            .background(CDColor.White40)
            .padding(1.dp)
            .background(CDColor.Grey)
            .padding(8.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .requiredWidth(BarDefaults.WidthWithPadding)
                    .height(2.dp)
                    .background(CDColor.Yellow)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "Expected count")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .requiredWidth(BarDefaults.Width)
                    .height(16.dp)
                    .drawWithCache {
                        onDrawWithContent {
                            drawRect(
                                brush = createStripeBrush(),
                                style = Fill,
                                size = DpSize(
                                    width = BarDefaults.Width,
                                    height = 16.dp
                                ).toSize()
                            )
                        }
                    }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "Random roll count")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .requiredWidth(BarDefaults.Width)
                    .height(16.dp)
                    .background(CDColor.Red)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "Roll count")
        }

        Text(text = "Total roll count: $turns")
    }
}

@Composable
private fun Graph(
    modifier: Modifier,
    state: State
) {
    Row(
        modifier = modifier
            .background(CDColor.White40)
            .padding(start = 2.dp, bottom = 2.dp)
            .background(CDColor.Grey),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        for (twoDiceSum in TwoDiceSum.values()) {
            Bar(
                twoDiceSum = twoDiceSum,
                count = state.twoDiceSumCount.getOrDefault(twoDiceSum, defaultValue = Count(0, 0)),
                maxCount = state.maxCount,
                turns = state.turns
            )
        }
    }
}

object BarDefaults {
    private val HorizontalPadding = 5.dp
    val ExpectedCountThickness = 2.dp
    val Width = 40.dp
    val WidthWithPadding = Width + HorizontalPadding
    val TopSpacing = 40.dp
}

@Composable
private fun Bar(
    twoDiceSum: TwoDiceSum,
    count: Count,
    maxCount: Int,
    turns: Int
) {
    Column(
        modifier = Modifier
            .requiredWidth(BarDefaults.WidthWithPadding)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        BoxWithConstraints(
            modifier = Modifier
                .weight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            val maxBarHeight = maxHeight - BarDefaults.TopSpacing
            val barHeight = maxBarHeight * (count.totalCount / maxCount.toFloat())
            val randomBarHeight = maxBarHeight * (count.randomCount / maxCount.toFloat())

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredSize(width = BarDefaults.Width, height = barHeight)
                    .background(color = CDColor.Red)
                    .drawWithCache {
                        onDrawWithContent {
                            drawRect(
                                brush = createStripeBrush(),
                                style = Fill,
                                size = DpSize(
                                    width = BarDefaults.Width,
                                    height = randomBarHeight
                                ).toSize()
                            )
                        }
                    }
            )

            val expectedCount = twoDiceSum.chance.times(turns)
            val expectedLineHeight = maxBarHeight * (expectedCount / maxCount.toFloat())

            Box(
                modifier = Modifier
                    .height(BarDefaults.ExpectedCountThickness)
                    .fillMaxWidth()
                    .offset(y = -expectedLineHeight - BarDefaults.ExpectedCountThickness)
                    .background(CDColor.Yellow)
            )

            Text(
                modifier = Modifier
                    .offset(y = -barHeight),
                textAlign = TextAlign.Center,
                text = count.totalCount.toString()
            )
        }
    }
}

private fun Density.createStripeBrush(): Brush {
    val stripeWidthPx = 4.dp.toPx()
    val brushSizePx = stripeWidthPx + stripeWidthPx
    val stripeStart = stripeWidthPx / brushSizePx

    return Brush.linearGradient(
        stripeStart to Color.Transparent,
        stripeStart to CDColor.DarkRed,
        start = Offset(0f, 0f),
        end = Offset(brushSizePx, brushSizePx),
        tileMode = TileMode.Repeated
    )
}

private val PreviewState = State(
    twoDiceSumCount = TwoDiceSum.values().associateWith {
        val totalCount = Random.nextInt(10)
        Count(
            totalCount = totalCount,
            randomCount = Random.nextInt(totalCount.coerceAtLeast(1))
        )
    }
)

@Composable
@Preview(name = "mobile", widthDp = 891, heightDp = 411, device = Devices.AUTOMOTIVE_1024p)
@Preview(name = "mobileSmall", widthDp = 640, heightDp = 360, device = Devices.AUTOMOTIVE_1024p)
private fun Preview() {
    CDTheme {
        StatsScreen(
            onNavigationIconClick = { },
            state = PreviewState
        )
    }
}
