package com.tanexc.podsify.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Config {
    @Serializable
    data object MainScreen: Config()

    @Serializable
    data object DeviceScreen: Config()
}