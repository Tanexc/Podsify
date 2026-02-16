package com.tanexc.podsify.presentation.screen.main.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.tanexc.bluetoothtool.BluetoothTool
import com.tanexc.bluetoothtool.domain.ConnectionState
import com.tanexc.podsify.presentation.navigation.BaseComponent
import com.tanexc.podsify.presentation.navigation.Config
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainScreenComponent(
    context: ComponentContext,
    onNavigate: (Config) -> Unit,
    onBack: () -> Unit,
    val bluetoothTool: BluetoothTool
) : BaseComponent(context, onNavigate, onBack) {
    val connectionState: StateFlow<ConnectionState> = bluetoothTool.connectionState

    val permissionState: StateFlow<PermissionState>
        field = MutableStateFlow<PermissionState>(PermissionState.Unknown)

    fun updatePermissionsState(state: PermissionState) {
        permissionState.value = state
    }

    fun startBluetoothTool() {
        bluetoothTool.start()
        lifecycle.doOnDestroy { stopBluetoothTool() }
    }

    fun stopBluetoothTool() {
        bluetoothTool.stop()
    }
}