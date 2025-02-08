package com.morgan.movies.di

import com.morgan.movies.core.MyInterceptor
import com.morgan.movies.core.utils.Constant
import com.morgan.movies.data.api.MoviesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttp () : OkHttpClient{
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(MyInterceptor())
            .addNetworkInterceptor(logger)
            .connectTimeout(3,TimeUnit.MINUTES)
            .readTimeout(3,TimeUnit.MINUTES)
            .writeTimeout(3,TimeUnit.MINUTES)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesApi(retrofit:Retrofit):MoviesApi =
         retrofit.create(MoviesApi::class.java)

}