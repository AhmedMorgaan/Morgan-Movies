package com.morgan.movies.core.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.morgan.movies.core.utils.Constant.Companion.REQUEST_Notification

object Permission {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun verifyNotificationPermission(activity: Activity): Boolean {
        val notificationPermission =
            ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            )
        return if (notificationPermission != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf<String?>(Manifest.permission.POST_NOTIFICATIONS)
            ActivityCompat.requestPermissions(
                activity,
                permissions,
                REQUEST_Notification
            )
            false
        } else {
            true
        }
    }
}