package com.morgan.movies.ui.home

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.morgan.movies.data.repositories.MoviesRepository
import com.morgan.movies.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
):BaseViewModel() {
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