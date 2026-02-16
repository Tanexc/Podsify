package com.tanexc.bluetoothtool.domain.repository

import com.tanexc.bluetoothtool.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun getConnectedDevice(): Flow<Device?>
}