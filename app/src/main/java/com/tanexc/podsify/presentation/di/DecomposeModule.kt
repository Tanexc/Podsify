package com.tanexc.podsify.presentation.di

import com.arkivanov.decompose.ComponentContext
import com.tanexc.podsify.presentation.navigation.BaseComponent
import com.tanexc.podsify.presentation.navigation.Config
import com.tanexc.podsify.presentation.screen.main.component.MainScreenComponent
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.mp.KoinPlatform.getKoin

val decomposeModule = module {
    factory { (context: ComponentContext, onNavigate: (Config) -> Unit, onBack: () -> Unit) ->
        MainScreenComponent(
            context = context,
            onNavigate = onNavigate,
            onBack = onBack,
            bluetoothTool = get()
        )
    }
}

inline fun <reified T : BaseComponent> getComponent(vararg parameters: Any?) = getKoin().get<T> {
    parametersOf(*parameters)
}