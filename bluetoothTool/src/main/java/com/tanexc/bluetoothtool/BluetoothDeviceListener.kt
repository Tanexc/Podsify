package com.tanexc.bluetoothtool

import android.Manifest
import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import com.tanexc.bluetoothtool.domain.Battery
import com.tanexc.bluetoothtool.domain.ConnectionState
import com.tanexc.bluetoothtool.domain.Device
import com.tanexc.bluetoothtool.utils.getHeadphoneManufacturer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BluetoothDeviceListener(
    private val context: Context
) {
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var a2dpProxy: BluetoothProfile? = null

    val connectionState: StateFlow<ConnectionState>
        field = MutableStateFlow<ConnectionState>(ConnectionState.NoConnection)

    fun initialize(): Boolean {
        context.getSystemService<BluetoothManager>()?.let {
            bluetoothManager = it
        } ?: return false
        bluetoothAdapter = bluetoothManager.adapter

        bluetoothAdapter.getProfileProxy(context, bluetoothListener, BluetoothProfile.A2DP)
        context.registerReceiver(connectionReceiver, intentFilter)

        return true
    }

    fun stop() {
        bluetoothAdapter.closeProfileProxy(BluetoothProfile.A2DP, a2dpProxy)
    }

    val bluetoothListener = object : BluetoothProfile.ServiceListener {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
            a2dpProxy = proxy
            updateConnectionState()
        }

        override fun onServiceDisconnected(profile: Int) {
            connectionState.value = ConnectionState.NoConnection
            a2dpProxy = null
        }
    }

    val connectionReceiver = object : BroadcastReceiver() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onReceive(context: Context?, intent: Intent?) {
            updateConnectionState()
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun updateConnectionState() {
        a2dpProxy?.connectedDevices?.firstOrNull()?.let { bluetoothDevice ->
            val getBattery = bluetoothDevice.javaClass.getMethod("getBatteryLevel")
            val batteryLevel = (getBattery(bluetoothDevice) as? Int) ?: -1
            val battery =
                if (batteryLevel != -1) Battery.HeadPhoneBattery(batteryLevel)
                else Battery.Undefined
            val device = Device(
                name = bluetoothDevice.name,
                address = bluetoothDevice.address,
                battery = battery,
                manufacturer = bluetoothDevice.getHeadphoneManufacturer()
            )
            connectionState.value = ConnectionState.Connected(device)
        } ?: run {
            connectionState.value = ConnectionState.NoConnection
        }
    }

    private val intentFilter = IntentFilter().apply {
        addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)
    }

}