package com.tanexc.bluetoothtool.data.repository

import com.tanexc.bluetoothtool.data.ConnectedDeviceMonitor
import com.tanexc.bluetoothtool.domain.model.Device
import com.tanexc.bluetoothtool.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow

internal class DeviceRepositoryImpl(
    val connectedDeviceMonitor: ConnectedDeviceMonitor
): DeviceRepository {
    override fun getConnectedDevice(): Flow<Device?> = connectedDeviceMonitor.connectedDevice
}