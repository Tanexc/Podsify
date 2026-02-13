package com.tanexc.bluetoothtool.utils

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.util.Log
import com.tanexc.bluetoothtool.domain.Battery
import com.tanexc.bluetoothtool.domain.Manufacturer

sealed class DeviceBatteryCallback(
    protected val deviceName: String = "",
    protected val onBatteryUpdate: (Battery) -> Unit = {}
) : ScanCallback() {
    protected abstract val MANUFACTURER_ID: Int
    protected abstract val MANUFACTURER_DATA: ByteArray

    abstract fun parseScanResults(results: List<ScanResult>): Battery

    val scanFilters: List<ScanFilter> = listOf(
        ScanFilter.Builder()
            .setDeviceName(deviceName)
            .build(),
        ScanFilter.Builder()
            .setManufacturerData(MANUFACTURER_ID, MANUFACTURER_DATA)
            .build()
    )

    override fun onBatchScanResults(results: List<ScanResult?>?) {
        super.onBatchScanResults(results)
        results
            ?.filterNotNull()
            ?.let { preparedResults ->
                val battery = parseScanResults(preparedResults)
                if (battery !is Battery.Undefined) onBatteryUpdate(battery)
            }
    }

    class AppleCallback(
        deviceName: String,
        onBatteryUpdate: (Battery) -> Unit
    ) : DeviceBatteryCallback(deviceName, onBatteryUpdate) {
        override val MANUFACTURER_ID = 0x004C
        override val MANUFACTURER_DATA = byteArrayOf(0x07, 0x19)

        private val PRODUCT_IDS = mapOf(
            0x0220 to "AirPods",
            0x0F20 to "AirPods 2",
            0x1320 to "AirPods 3",
            0x0E20 to "AirPods Pro",
            0x1420 to "AirPods Pro 2",
            0x2401 to "AirPods Pro 2",
            0x2420 to "AirPods Pro 2",
            0x2720 to "AirPods Pro 3",

            )

        override fun parseScanResults(results: List<ScanResult>): Battery {
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
    }

    object EmptyCallback : DeviceBatteryCallback() {
        override val MANUFACTURER_ID = -1
        override val MANUFACTURER_DATA = byteArrayOf(0x00, 0x00)

        override fun parseScanResults(results: List<ScanResult>): Battery = Battery.Undefined
    }

    companion object {
        fun callback(
            manufacturer: Manufacturer,
            deviceName: String,
            onBatteryUpdate: (Battery) -> Unit
        ): DeviceBatteryCallback = when (manufacturer) {
            Manufacturer.Apple -> AppleCallback(deviceName, onBatteryUpdate)
            Manufacturer.Samsung -> EmptyCallback
            Manufacturer.Xiaomi -> EmptyCallback
            Manufacturer.Marshall -> EmptyCallback
            Manufacturer.Meizu -> EmptyCallback
            Manufacturer.Huawei -> EmptyCallback
            Manufacturer.Sony -> EmptyCallback
            Manufacturer.Unknown -> EmptyCallback
        }
    }
}