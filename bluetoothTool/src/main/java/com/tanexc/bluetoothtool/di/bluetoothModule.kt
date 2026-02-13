package com.tanexc.bluetoothtool.di

import com.tanexc.bluetoothtool.BluetoothDeviceListener
import com.tanexc.bluetoothtool.BluetoothTool
import com.tanexc.bluetoothtool.DeviceBatteryScanner
import com.tanexc.bluetoothtool.domain.usecase.GetConnectionStateUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val bluetoothModule = module {
    single { BluetoothDeviceListener(androidContext()) }
    single { DeviceBatteryScanner(androidContext()) }
    single { BluetoothTool(androidContext()) }

    singleOf(::GetConnectionStateUseCase)
}