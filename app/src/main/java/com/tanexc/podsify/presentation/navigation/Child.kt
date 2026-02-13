package com.tanexc.podsify.presentation.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.tanexc.podsify.presentation.di.getComponent
import com.tanexc.podsify.presentation.screen.device.DeviceScreen
import com.tanexc.podsify.presentation.screen.device.DeviceScreenComponent
import com.tanexc.podsify.presentation.screen.main.MainScreen
import com.tanexc.podsify.presentation.screen.main.component.MainScreenComponent

sealed interface Child {
    @Composable
    fun Content()

    data class MainScreenChild(val component: MainScreenComponent): Child {
        @Composable
        override fun Content() = MainScreen(component)
    }

    data class DeviceScreenChild(val component: DeviceScreenComponent): Child {
        @Composable
        override fun Content() = DeviceScreen(component)
    }

    companion object {
        fun create(
            config: Config,
            context: ComponentContext,
            onNavigate: (Config) -> Unit,
            onBack: () -> Unit
        ): Child = when (config) {
            is Config.DeviceScreen -> DeviceScreenChild(getComponent(context, onNavigate, onBack))
            is Config.MainScreen -> MainScreenChild(getComponent(context, onNavigate, onBack))
        }
    }
}