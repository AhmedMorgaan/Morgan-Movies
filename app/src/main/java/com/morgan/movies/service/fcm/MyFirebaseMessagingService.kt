package com.morgan.movies.service.fcm

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.morgan.movies.R
import com.morgan.movies.data.models.NotificationModel
import com.morgan.movies.ui.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            Log.e("remoteMessage.notification", "${remoteMessage.notification?.title}")
            Log.e("remoteMessage.notification", "${remoteMessage.notification?.body}")
            val pushNotificationModel = NotificationModel(
                title = remoteMessage.notification?.title,
                message = remoteMessage.notification?.body
            )
            handleDataMessage(pushNotificationModel)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
    }

    private fun handleDataMessage(pushNotificationModelResponse: NotificationModel) {
        try {

            // app is in splash, show the notification in notification tray
            // check for image attachment
            NewNotificationMessage.notify(
                this,
                Intent(this, MainActivity::class.java),
                pushNotificationModelResponse,
                R.drawable.morgan_logo_400x400
            )
        } catch (e: Exception) {
            Log.e("Exception: ", e.message.toString())
        }
    }

    companion object {
        private val TAG = MyFirebaseMessagingService::class.java.simpleName
    }
}
