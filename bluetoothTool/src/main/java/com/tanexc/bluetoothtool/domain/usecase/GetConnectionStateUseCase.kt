package com.tanexc.bluetoothtool.domain.usecase

import android.util.Log
import com.tanexc.bluetoothtool.domain.ConnectionState
import com.tanexc.bluetoothtool.domain.repository.DeviceBatteryRepository
import com.tanexc.bluetoothtool.domain.repository.DeviceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

internal class GetConnectionStateUseCase(
    private val deviceBatteryRepository: DeviceBatteryRepository,
    private val deviceRepository: DeviceRepository
) {
    operator fun invoke(): Flow<ConnectionState> = deviceRepository
        .getConnectedDevice()
        .flatMapLatest { device ->
            device?.let {
                deviceBatteryRepository.getDeviceBattery(device.address, device.manufacturer)
                    .map { battery ->
                        device.copy(battery = battery)
                    }
            } ?: flowOf(null)

        }
        .map { device ->
            device?.let {
                ConnectionState.Connected(it)
            } ?: ConnectionState.NoConnection
        }
}