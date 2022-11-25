package com.wirne.catandice.data.model

import androidx.compose.runtime.Immutable
import kotlin.time.Duration

@Immutable
data class Timer(
    val time: Duration,
    val timeLeft: Duration,
    val enabled: Boolean
)