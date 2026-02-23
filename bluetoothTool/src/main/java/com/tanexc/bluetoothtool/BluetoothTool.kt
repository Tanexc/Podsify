package com.tanexc.bluetoothtool

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Stable
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.tanexc.bluetoothtool.domain.ConnectionState
import com.tanexc.bluetoothtool.worker.BluetoothWorker
import kotlinx.coroutines.flow.StateFlow

@Stable
class BluetoothTool(
    context: Context
) {
    private val workTag = "BluetoothToolWork"
    private val workManager = WorkManager.getInstance(context.applicationContext)

    @RequiresApi(Build.VERSION_CODES.O)
    fun start() {
        val request = OneTimeWorkRequestBuilder<BluetoothWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        workManager.beginUniqueWork(
            uniqueWorkName = workTag,
            existingWorkPolicy = ExistingWorkPolicy.KEEP,
            request = request,
        ).enqueue()
    }

    fun stop() = workManager.cancelAllWorkByTag(workTag)

    val connectionState: StateFlow<ConnectionState> = BluetoothWorker.connectionState
}