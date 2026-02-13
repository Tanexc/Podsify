package com.tanexc.bluetoothtool.domain.usecase

import com.tanexc.bluetoothtool.BluetoothTool
import com.tanexc.bluetoothtool.domain.ConnectionState
import kotlinx.coroutines.flow.StateFlow

class GetConnectionStateUseCase(
    private val bluetoothTool: BluetoothTool
) {
    operator fun invoke(): StateFlow<ConnectionState> = bluetoothTool.connectionState
}