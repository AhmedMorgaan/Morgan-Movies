package com.morgan.movies.data.base

import android.content.Context
import com.morgan.movies.R
import com.morgan.movies.core.Result
import com.morgan.movies.core.utils.NetworkHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject

/**
 * Network operation executor for retrofit library
 */
class RetrofitExecutor @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun <T : Any> makeRequest(
        call: suspend () -> Response<T>
    ): Result<T> {
        return if (NetworkHelper().isConnected(context)) {
            try {
                val data = safeApiResult(call)
                return when (data.code()) {
                    200 -> {
                        Result.Success(data.body()!!)
                    }
                    else -> {
                        Result.Error(Exception(data.message()))
                    }
                }
            } catch (e: Exception) {
                Result.Error(Exception(e.localizedMessage))
            }
        } else {
            Result.Error(Exception(context.getString(R.string.no_internet_connection)))
        }
    }

    private suspend fun <T : Any> safeApiResult(
        call: suspend () -> Response<T>
    ): Response<T> {
        val response = call()
        return when (response.isSuccessful) {
            false -> throw Exception(context.getString(R.string.something_went_wrong))
            else -> response
        }
    }
}