package com.tanexc.podsify.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.tanexc.podsify.presentation.navigation.Child.Companion.create

class RootComponent(
    componentContext: ComponentContext,
): ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    val childStack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.MainScreen,
        handleBackButton = true,
        childFactory = { config, context ->
            create(
                config = config,
                context = context,
                onNavigate = ::onNavigate,
                onBack = ::onBack
            )
        }
    )

    fun onNavigate(config: Config) {
        navigation.bringToFront(config)
    }

    fun onBack() {
        navigation.pop()
    }
}