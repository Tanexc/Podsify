package com.tanexc.podsify

import android.app.Application
import com.tanexc.bluetoothtool.di.bluetoothModule
import com.tanexc.podsify.presentation.di.decomposeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PodsifyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PodsifyApplication)
            modules(decomposeModule, bluetoothModule)
        }
    }
}