package com.morgan.movies.ui.splash

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.morgan.movies.ui.base.BaseViewModel

class SplashViewModel : BaseViewModel() {

    fun requestFirebaseTokenWithoutLogin() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Get and save token
                val token = task.result
                //login()
                Log.e("FirebaseMessaging", "FirebaseToken $token ", )
            } else {
                // Handle error
                Log.e("FirebaseMessaging", "Error getting token", task.exception)
            }
        }
    }
}