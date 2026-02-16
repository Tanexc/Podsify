package com.tanexc.bluetoothtool

import android.content.Context
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

    fun start() {
        val request = OneTimeWorkRequestBuilder<BluetoothWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        workManager.beginUniqueWork(
            uniqueWorkName = workTag,
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            request = request,
        ).enqueue()
    }

    fun stop() {
        workManager.cancelAllWorkByTag(workTag)
    }

    val connectionState: StateFlow<ConnectionState> = BluetoothWorker.connectionState
}