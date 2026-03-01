package com.tanexc.bluetoothtool.worker

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.tanexc.bluetoothtool.R
import com.tanexc.bluetoothtool.domain.ApplicationLaunchHelper
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

    private val applicationLaunchHelper: ApplicationLaunchHelper by inject()

    private val baseNotificationBuilder = NotificationCompat
        .Builder(appContext, CHANNEL_ID)
        .setSmallIcon(R.drawable.small_icon)
        .setOngoing(true)
        .setContentIntent(applicationLaunchHelper.provideLaunchIntent())

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

                else -> {
                    updateNotification()
                }
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

        const val CHANNEL_ID = "Podsify notifications"
        private const val NOTIFICATION_ID = 1
    }
}