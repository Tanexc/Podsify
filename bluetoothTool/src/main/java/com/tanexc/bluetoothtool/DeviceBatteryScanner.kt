package com.tanexc.bluetoothtool

import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import com.tanexc.bluetoothtool.domain.Battery
import com.tanexc.bluetoothtool.domain.Manufacturer
import com.tanexc.bluetoothtool.utils.DeviceBatteryCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DeviceBatteryScanner(
    private val context: Context
) {
    private val bluetoothManager by lazy {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    private val bluetoothAdapter by lazy { bluetoothManager.adapter }
    private val bluetoothLeScanner by lazy { bluetoothAdapter.bluetoothLeScanner }

    private var callback: DeviceBatteryCallback? = null
    var isScanning = false
        private set(value) { field = value }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @RequiresPermission(android.Manifest.permission.BLUETOOTH_SCAN)
    fun startBatteryScan(
        deviceName: String,
        manufacturer: Manufacturer,
        updateIntervalMillis: Long = 2000
    ): Flow<Battery> = callbackFlow {
        if (!bluetoothAdapter.isEnabled) {
            return@callbackFlow
        }

        if (isScanning) {
            stopBatteryScan()
        }

        isScanning = true

        callback = DeviceBatteryCallback.callback(manufacturer, deviceName) { battery ->
            trySend(battery)
        }

        val scanSettings = buildScanSettings(updateIntervalMillis)

        bluetoothLeScanner.startScan(
            callback?.scanFilters,
            scanSettings,
            callback
        )

        awaitClose {
            stopBatteryScan()
        }
    }

    @RequiresPermission(android.Manifest.permission.BLUETOOTH_SCAN)
    fun stopBatteryScan() {
        if (!isScanning) return
        bluetoothLeScanner.stopScan(callback)
        isScanning = false
    }

    private fun buildScanSettings(reportDelayMillis: Long): ScanSettings {
        return ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(reportDelayMillis)
            .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
            .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                }
            }
            .build()
    }
}