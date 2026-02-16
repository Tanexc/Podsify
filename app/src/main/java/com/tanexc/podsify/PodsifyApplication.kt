package com.tanexc.podsify

import android.app.Application
import androidx.glance.appwidget.updateAll
import com.tanexc.bluetoothtool.BluetoothTool
import com.tanexc.bluetoothtool.di.bluetoothModule
import com.tanexc.podsify.presentation.di.decomposeModule
import com.tanexc.podsify.widgets.PodsifyWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PodsifyApplication : Application() {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PodsifyApplication)
            modules(decomposeModule, bluetoothModule)
        }

        scope.launch {
            val bluetoothTool = get<BluetoothTool>()
            val widget = PodsifyWidget()

            bluetoothTool.connectionState.collectLatest {
                widget.updateAll(this@PodsifyApplication)
            }
        }
    }
}