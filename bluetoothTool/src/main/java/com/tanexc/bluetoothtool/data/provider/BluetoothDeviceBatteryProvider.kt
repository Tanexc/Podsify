package com.tanexc.bluetoothtool.data.provider

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.tanexc.bluetoothtool.domain.DeviceBatteryProvider
import com.tanexc.bluetoothtool.domain.model.Battery
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BluetoothDeviceBatteryProvider : DeviceBatteryProvider, KoinComponent {
    private val context: Context by inject()
    private val bluetoothAdapter: BluetoothAdapter by inject()

    override fun provideBattery(
        address: String
    ): Flow<Battery> = callbackFlow {
        val bluetoothDevice = bluetoothAdapter.getRemoteDevice(address)

        send(bluetoothDevice.getBattery())

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    "android.bluetooth.device.action.BATTERY_LEVEL_CHANGED" -> {
                        val batteryLevel = intent.getIntExtra(
                            "android.bluetooth.device.extra.BATTERY_LEVEL", -1
                        )
                        trySend(Battery.HeadPhoneBattery(batteryLevel))
                    }
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val filter = IntentFilter("android.bluetooth.device.action.BATTERY_LEVEL_CHANGED")
            context.applicationContext.registerReceiver(
                receiver,
                filter, RECEIVER_EXPORTED
            )
        } else return@callbackFlow

        awaitClose {
            context.applicationContext.unregisterReceiver(receiver)
        }
    }

    fun BluetoothDevice.getBattery(): Battery {
        val getBatteryLevel = BluetoothDevice::class.java.getMethod("getBatteryLevel")
        val level = (getBatteryLevel.invoke(this@getBattery) as? Int) ?: return Battery.Undefined

        return Battery.HeadPhoneBattery(level)
    }
}