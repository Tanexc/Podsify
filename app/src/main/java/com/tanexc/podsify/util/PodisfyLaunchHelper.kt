package com.tanexc.podsify.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tanexc.bluetoothtool.domain.ApplicationLaunchHelper
import com.tanexc.podsify.MainActivity

class PodsifyLaunchHelper(
    context: Context
): ApplicationLaunchHelper {
    private val intent = Intent().apply {
        setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        setClass(context.applicationContext, MainActivity::class.java)
    }
    private val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    override fun provideLaunchIntent(): PendingIntent = pendingIntent
}