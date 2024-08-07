package com.example.ds_movies.core

import com.example.ds_movies.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_TOKEN}")
            .build()
        return chain.proceed(request)
    }
}