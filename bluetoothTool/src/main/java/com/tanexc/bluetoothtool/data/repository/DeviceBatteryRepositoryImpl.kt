package com.tanexc.bluetoothtool.data.repository

import com.tanexc.bluetoothtool.domain.DeviceBatteryProvider
import com.tanexc.bluetoothtool.domain.model.Battery
import com.tanexc.bluetoothtool.domain.model.Manufacturer
import com.tanexc.bluetoothtool.domain.repository.DeviceBatteryRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

internal class DeviceBatteryRepositoryImpl : DeviceBatteryRepository, KoinComponent {
    override fun getDeviceBattery(
        address: String,
        manufacturer: Manufacturer
    ): Flow<Battery> =
        get<DeviceBatteryProvider>(parameters = { parametersOf(manufacturer) })
            .provideBattery(address)
}