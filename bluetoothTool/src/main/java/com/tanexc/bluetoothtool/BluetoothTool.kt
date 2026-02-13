package com.tanexc.bluetoothtool

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Stable
import com.tanexc.bluetoothtool.domain.ConnectionState
import com.tanexc.bluetoothtool.domain.ServiceState
import com.tanexc.bluetoothtool.service.BluetoothService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach

@Stable
class BluetoothTool(
    val context: Context
) {
    private val intent = Intent(context.applicationContext, BluetoothService::class.java).apply {
        action = BluetoothService.START_SERVICE_ACTION
    }

    fun start() = context.applicationContext.startService(intent)

    fun stop() {
        val intent = Intent(context.applicationContext, BluetoothService::class.java)
        context.applicationContext.stopService(intent)
    }

    val serviceState: StateFlow<ServiceState> = BluetoothService.serviceState

    val connectionState: StateFlow<ConnectionState> = BluetoothService.connectionState

}