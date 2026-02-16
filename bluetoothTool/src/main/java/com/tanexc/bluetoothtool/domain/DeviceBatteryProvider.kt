package com.tanexc.bluetoothtool.domain

import com.tanexc.bluetoothtool.domain.model.Battery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface DeviceBatteryProvider {
    fun provideBattery(
        address: String
    ): Flow<Battery>

    companion object UndefinedBatteryProvider: DeviceBatteryProvider {
        override fun provideBattery(
            address: String
        ): Flow<Battery> = flowOf(Battery.Undefined)
    }
}