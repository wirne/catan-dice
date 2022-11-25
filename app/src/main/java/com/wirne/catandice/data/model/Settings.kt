package com.wirne.catandice.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class Settings(
    val citiesAndKnightsEnabled: Boolean,
    val randomPercentage: Int,
)
