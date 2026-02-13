package com.tanexc.podsify.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class BaseComponent(
    val context: ComponentContext,
    val onNavigate: (Config) -> Unit,
    val onBack: () -> Unit
): ComponentContext by context {
    val componentScope: CoroutineScope
        get() = CoroutineScope(SupervisorJob() + Dispatchers.IO).withLifecycle(lifecycle)
}