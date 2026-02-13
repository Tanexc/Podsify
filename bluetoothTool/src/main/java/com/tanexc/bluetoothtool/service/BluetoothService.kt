package com.tanexc.bluetoothtool.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tanexc.bluetoothtool.BluetoothDeviceListener
import com.tanexc.bluetoothtool.DeviceBatteryScanner
import com.tanexc.bluetoothtool.R
import com.tanexc.bluetoothtool.domain.ConnectionState
import com.tanexc.bluetoothtool.domain.ServiceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.jvm.java


internal class BluetoothService : Service() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val bluetoothDeviceListener: BluetoothDeviceListener by inject()
    private lateinit var notificationManager: NotificationManagerCompat

    private val deviceBatteryScanner: DeviceBatteryScanner by inject()

    private val baseNotificationBuilder = NotificationCompat
        .Builder(this, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
        .setOngoing(true)


    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            START_SERVICE_ACTION -> startService()
            else -> {}
        }

        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private fun startService() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.service_title))
            .setContentText(getString(R.string.service_text))
            .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
            .setOngoing(true)
            .build()
        startForeground(NOTIFICATION_ID, notification)

        _serviceState.value = ServiceState.Running

        if (bluetoothDeviceListener.initialize()) {
            scope.launch {
                bluetoothDeviceListener.connectionState.collectLatest { state ->
                    _connectionState.value = state

                    when (state) {
                        is ConnectionState.Connected -> {
                            updateNotification(
                                state.device.name,
                                state.device.battery.toString()
                            )
                            if (deviceBatteryScanner.isScanning) {
                                deviceBatteryScanner.stopBatteryScan()
                            }

                            val device = state.device

                            deviceBatteryScanner
                                .startBatteryScan(device.name, device.manufacturer)
                                .distinctUntilChanged()
                                .collectLatest { deviceBattery ->
                                    _connectionState.value = ConnectionState.Connected(device.copy(battery = deviceBattery))
                                    updateNotification(
                                        title = device.name,
                                        text = deviceBattery.toString()
                                    )
                                }
                        }

                        else -> {
                            deviceBatteryScanner.stopBatteryScan()
                            updateNotification()
                        }
                    }
                }
            }
        } else {
            stopService()
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private fun stopService() {
        bluetoothDeviceListener.stop()
        deviceBatteryScanner.stopBatteryScan()
        _serviceState.value = ServiceState.Stopped
        scope.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    @SuppressLint("MissingPermission")
    private fun updateNotification(
        title: String = getString(R.string.service_title),
        text: String = getString(R.string.service_text)
    ) {
        val notification = baseNotificationBuilder
            .setContentText(text)
            .setContentTitle(title)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private val _serviceState = MutableStateFlow(ServiceState.Stopped)
        val serviceState: StateFlow<ServiceState> = _serviceState

        private val _connectionState =
            MutableStateFlow<ConnectionState>(ConnectionState.NoConnection)
        val connectionState: StateFlow<ConnectionState> = _connectionState

        const val START_SERVICE_ACTION = "START_SERVICE_ACTION"
        private const val CHANNEL_ID = "bluetooth_service_channel"
        private const val NOTIFICATION_ID = 1
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        stopService()
    }
}