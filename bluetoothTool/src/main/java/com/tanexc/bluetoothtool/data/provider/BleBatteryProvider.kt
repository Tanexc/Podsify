package com.tanexc.bluetoothtool.data.provider

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.util.Log
import androidx.annotation.RequiresPermission
import com.tanexc.bluetoothtool.domain.DeviceBatteryProvider
import com.tanexc.bluetoothtool.domain.model.Battery
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BleBatteryProvider : DeviceBatteryProvider, KoinComponent {
    private val bluetoothLeScanner: BluetoothLeScanner by inject()
    private val bluetoothAdapter: BluetoothAdapter by inject()

    private val scanSettings: ScanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .setReportDelay(5_000)
        .setNumOfMatches(ScanSettings.MATCH_NUM_MAX_ADVERTISEMENT)
        .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
        .apply {
            setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
        }
        .build()

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])
    override fun provideBattery(
        address: String
    ): Flow<Battery> = callbackFlow {
        val bluetoothDevice = bluetoothAdapter.getRemoteDevice(address)
        send(Battery.Undefined)

        val scanFilters: List<ScanFilter> = listOf(
            ScanFilter.Builder()
                .setDeviceName(bluetoothDevice.name)
                .build(),
            ScanFilter.Builder()
                .setManufacturerData(MANUFACTURER_ID, MANUFACTURER_DATA)
                .build()
        )

        val callback: ScanCallback = object : ScanCallback() {
            override fun onBatchScanResults(results: List<ScanResult?>?) {
                super.onBatchScanResults(results)
                Log.i("cucu", "parse apple batch")
                results
                    ?.filterNotNull()
                    ?.let { preparedResults ->
                        val battery = parseScanResults(preparedResults)
                        if (battery !is Battery.Undefined) trySend(battery)
                    }
            }
        }

        bluetoothLeScanner.startScan(scanFilters, scanSettings, callback)
        awaitClose {
            Log.i("cucu", "parse apple closed")
            bluetoothLeScanner.stopScan(callback)
        }
    }

    fun parseScanResults(results: List<ScanResult>): Battery {
        Log.i("cucu", "parse apple")
        val validData = results
            .mapNotNull { result ->
                result.scanRecord?.let {
                    val rssi = result.rssi

                    if (rssi < -60) return@let null

                    val data = result.scanRecord?.manufacturerSpecificData[MANUFACTURER_ID]
                        ?: byteArrayOf()


                    if (data.size < 27) return@let null
                    val productId =
                        ((data[3].toInt() and 0xFF) shl 8) or (data[2].toInt() and 0xFF)

                    if (productId !in PRODUCT_IDS.keys) return@let null

                    rssi to data
                }
            }
            .minByOrNull { it.first }
            ?.second

        validData?.let { data ->
            val shouldSwap = ((data[5].toInt() and 0xF0) shr 4 and 0x02) != 0

            val battery1 = data[6].toInt() and 0xF0 shr 4
            val battery2 = data[6].toInt() and 0x0F
            val batteryLeft = if (shouldSwap) battery2 else battery1
            val batteryRight = if (shouldSwap) battery1 else battery2
            val batteryCase = data[7].toInt() and 0x0F
            return Battery.EarBudsBattery(
                (batteryLeft * 10).takeIf { it <= 100 } ?: -1,
                (batteryRight * 10).takeIf { it <= 100 } ?: -1,
                (batteryCase * 10).takeIf { it <= 100 } ?: -1
            )
        }

        return Battery.Undefined
    }

    companion object {
        private const val MANUFACTURER_ID = 0x004C
        private val MANUFACTURER_DATA = byteArrayOf(0x07, 0x19)

        private val PRODUCT_IDS = mapOf(
            0x0220 to "AirPods",
            0x0F20 to "AirPods 2",
            0x1320 to "AirPods 3",
            0x0E20 to "AirPods Pro",
            0x1420 to "AirPods Pro 2",
            0x2401 to "AirPods Pro 2",
            0x2420 to "AirPods Pro 2",
            0x2720 to "AirPods Pro 3"
        )
    }
}