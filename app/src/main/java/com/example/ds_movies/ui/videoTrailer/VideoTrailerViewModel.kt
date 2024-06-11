package com.example.ds_movies.ui.videoTrailer

import android.util.Log
import com.example.ds_movies.core.Result
import com.example.ds_movies.data.models.VideoItem
import com.example.ds_movies.data.repositories.MoviesRepository
import com.example.ds_movies.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VideoTrailerViewModel @Inject constructor(
    val moviesRepository: MoviesRepository
) :BaseViewModel() {

    suspend fun getMovieVideo(movieId: Int?):MutableList<VideoItem>? {
        var videosList: MutableList<VideoItem>? = null
        val result = moviesRepository.getMovieVideo(movieId)
        try {
            progressBar.postValue(true)  //value = true
            when (result) {
                is Result.Success -> {
                    withContext(Dispatchers.Main){
                        progressBar.postValue(false)
                        videosList = result.data.results
                    }
                }
                is Result.Error -> {
                    withContext(Dispatchers.Main){
                        progressBar.postValue(false)
                        showMessage.postValue(result.exception.message)
                    }
                }
                is Result.Loading -> {
                    withContext(Dispatchers.Main){
                        progressBar.postValue(true)
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                progressBar.postValue(false)
                showMessage.postValue(e.localizedMessage)
                Log.e("apiCallError", e.message.toString())
            }

        }
        return videosList
    }
}