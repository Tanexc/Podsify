package com.tanexc.podsify.presentation.screen.device

import com.arkivanov.decompose.ComponentContext
import com.tanexc.podsify.presentation.navigation.BaseComponent
import com.tanexc.podsify.presentation.navigation.Config

class DeviceScreenComponent(
    context: ComponentContext,
    onNavigate: (Config) -> Unit,
    onBack: () -> Unit
) : BaseComponent(context, onNavigate, onBack) {

}