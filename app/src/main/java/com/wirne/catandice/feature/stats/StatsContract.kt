package com.wirne.catandice.feature.stats

import androidx.compose.runtime.Immutable
import com.wirne.catandice.common.ViewModelContract
import com.wirne.catandice.data.model.TwoDiceSum
import kotlin.math.roundToInt

interface StatsContract :
    ViewModelContract<StatsContract.Event, StatsContract.State> {
    object Event

    data class State(
        val twoDiceSumCount: Map<TwoDiceSum, Count>,
    ) {
        val turns: Int = twoDiceSumCount.values.sumOf { it.totalCount }

        val maxCount: Int =
            maxOf(
                twoDiceSumCount.values.maxOfOrNull { it.totalCount } ?: 0,
                (TwoDiceSum.Seven.chance * turns).roundToInt(),
            )

        companion object {
            val Initial = State(emptyMap())
        }
    }
}

@Immutable
data class Count(
    val totalCount: Int,
    val randomCount: Int,
)
