package com.tanexc.bluetoothtool.domain

import android.app.PendingIntent

/*
Used to provide PendingIntent from an app module that launches some an app component
*/
interface ApplicationLaunchHelper {
    fun provideLaunchIntent(): PendingIntent
}