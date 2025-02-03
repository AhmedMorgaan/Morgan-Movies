package com.morgan.movies.application

import android.app.Application
import com.morgan.movies.core.SharedPreference
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreference.init(this)
    }

}