package com.tanexc.bluetoothtool.worker

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.tanexc.bluetoothtool.R
import com.tanexc.bluetoothtool.domain.ConnectionState
import com.tanexc.bluetoothtool.domain.usecase.GetConnectionStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


internal class BluetoothWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {
    private val getConnectionStateUseCase: GetConnectionStateUseCase by inject()
    private val notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(applicationContext)

    private val baseNotificationBuilder = NotificationCompat
        .Builder(appContext, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.stat_sys_data_bluetooth)
        .setOngoing(true)

    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo())

        getConnectionStateUseCase().collect { state ->
            _connectionState.value = state
            when (state) {
                is ConnectionState.Connected -> {
                    updateNotification(
                        title = state.device.name,
                        text = state.device.battery.toString()
                    )
                }

                else -> updateNotification()
            }
        }
        return Result.retry()
    }

    private fun createForegroundInfo(): ForegroundInfo {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = updateNotification()

        val serviceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
        } else 0

        return ForegroundInfo(
            NOTIFICATION_ID,
            notification,
            serviceType
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_ID,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    private fun updateNotification(
        title: String = applicationContext.getString(R.string.service_title),
        text: String = applicationContext.getString(R.string.service_text)
    ): Notification = baseNotificationBuilder
        .setContentText(text)
        .setContentTitle(title)
        .build().also { notification ->
            notificationManager.notify(NOTIFICATION_ID, notification)
        }

    companion object {
        private val _connectionState =
            MutableStateFlow<ConnectionState>(ConnectionState.NoConnection)
        val connectionState: StateFlow<ConnectionState> = _connectionState

        private const val CHANNEL_ID = "Podsify notifications"
        private const val NOTIFICATION_ID = 1
    }
}