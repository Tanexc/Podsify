package com.tanexc.bluetoothtool.domain.repository

import com.tanexc.bluetoothtool.domain.model.Battery
import com.tanexc.bluetoothtool.domain.model.Manufacturer
import kotlinx.coroutines.flow.Flow

interface DeviceBatteryRepository {
    fun getDeviceBattery(
        address: String,
        manufacturer: Manufacturer
    ): Flow<Battery>
}