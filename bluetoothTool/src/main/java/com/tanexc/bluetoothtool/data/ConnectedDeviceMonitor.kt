package com.tanexc.bluetoothtool.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.util.Log
import com.tanexc.bluetoothtool.domain.model.Battery
import com.tanexc.bluetoothtool.domain.model.Device
import com.tanexc.bluetoothtool.utils.getManufacturer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.component.KoinComponent
import kotlin.properties.Delegates

internal class ConnectedDeviceMonitor(
    private val audioManager: AudioManager
) : KoinComponent {


    @SuppressLint("MissingPermission")
    val connectedDevice: Flow<Device?> = callbackFlow {
        send(null)

        var device: AudioDeviceInfo? by Delegates.observable(null) { _, _, audioDeviceInfo ->
            audioDeviceInfo?.let { info ->
                val device = Device(
                    name = info.productName.toString(),
                    address = info.address,
                    manufacturer = info.getManufacturer(),
                    battery = Battery.Undefined
                )
                trySend(device)
            } ?: trySend(null)

        }

        val connectionCallback = object : AudioDeviceCallback() {
            override fun onAudioDevicesAdded(addedDevices: Array<AudioDeviceInfo>) {
                super.onAudioDevicesAdded(addedDevices)
                addedDevices.firstOrNull { it.type in monitoredTypes }?.let {
                    device = it
                }
            }

            override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>) {
                super.onAudioDevicesRemoved(removedDevices)
                removedDevices.firstOrNull { it.address == device?.address }?.let {
                    device = null
                }
            }
        }

        audioManager.registerAudioDeviceCallback(connectionCallback, null)

        awaitClose {
            audioManager.unregisterAudioDeviceCallback(connectionCallback)
        }
    }


    private val monitoredTypes = listOf(
        AudioDeviceInfo.TYPE_BLE_HEADSET,
        AudioDeviceInfo.TYPE_BLUETOOTH_A2DP,
        AudioDeviceInfo.TYPE_BLE_SPEAKER,
    )
}