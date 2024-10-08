package com.wirne.catandice

import kotlinx.serialization.Serializable

@Serializable
sealed class CDRoute {

    @Serializable
    data object Game : CDRoute()

    @Serializable
    data object Stats : CDRoute()

    @Serializable
    data object Settings : CDRoute()
}
