package com.tanexc.bluetoothtool.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.media.AudioManager
import com.tanexc.bluetoothtool.BluetoothTool
import com.tanexc.bluetoothtool.data.ConnectedDeviceMonitor
import com.tanexc.bluetoothtool.data.provider.BleBatteryProvider
import com.tanexc.bluetoothtool.data.provider.BluetoothDeviceBatteryProvider
import com.tanexc.bluetoothtool.data.repository.DeviceBatteryRepositoryImpl
import com.tanexc.bluetoothtool.data.repository.DeviceRepositoryImpl
import com.tanexc.bluetoothtool.domain.DeviceBatteryProvider
import com.tanexc.bluetoothtool.domain.model.Manufacturer
import com.tanexc.bluetoothtool.domain.repository.DeviceBatteryRepository
import com.tanexc.bluetoothtool.domain.repository.DeviceRepository
import com.tanexc.bluetoothtool.domain.usecase.GetConnectionStateUseCase
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val bluetoothModule = module {
    single<BluetoothManager> { androidApplication().getSystemService(BluetoothManager::class.java) }
    single<AudioManager> { androidApplication().getSystemService(AudioManager::class.java) }

    single<BluetoothAdapter> { get<BluetoothManager>().adapter }
    single<BluetoothLeScanner> { get<BluetoothAdapter>().bluetoothLeScanner }

    singleOf(::ConnectedDeviceMonitor)

    factory<DeviceBatteryProvider> { (manufacturer: Manufacturer) ->
        when (manufacturer) {
            Manufacturer.Apple -> BleBatteryProvider()
            Manufacturer.Marshall -> BluetoothDeviceBatteryProvider()
            Manufacturer.Samsung -> DeviceBatteryProvider.UndefinedBatteryProvider
            Manufacturer.Xiaomi -> DeviceBatteryProvider.UndefinedBatteryProvider
            Manufacturer.Meizu -> DeviceBatteryProvider.UndefinedBatteryProvider
            Manufacturer.Huawei -> DeviceBatteryProvider.UndefinedBatteryProvider
            Manufacturer.Sony -> DeviceBatteryProvider.UndefinedBatteryProvider
            Manufacturer.Unknown -> DeviceBatteryProvider.UndefinedBatteryProvider
        }
    }

    singleOf(::DeviceBatteryRepositoryImpl) bind DeviceBatteryRepository::class
    singleOf(::DeviceRepositoryImpl) bind DeviceRepository::class

    singleOf(::GetConnectionStateUseCase)
    singleOf(::BluetoothTool)
}