package com.example.ds_movies.data

import android.app.Application
import com.example.ds_movies.core.SharedPreference
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreference.init(this)
    }

}