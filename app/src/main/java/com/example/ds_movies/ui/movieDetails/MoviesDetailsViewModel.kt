package com.example.ds_movies.ui.movieDetails

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ds_movies.core.Result
import com.example.ds_movies.core.SharedPreference
import com.example.ds_movies.core.utils.Constant
import com.example.ds_movies.data.models.Cast
import com.example.ds_movies.data.models.CategoryResponse
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.data.paging.SimilarMovieSource
import com.example.ds_movies.data.repositories.MoviesRepository
import com.example.ds_movies.ui.base.BaseViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MoviesDetailsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : BaseViewModel() {

    fun getSimilarMovies(movieId:Int?): Flow<PagingData<MovieItem>> {
        val similarMoviesListPaging = Pager(PagingConfig(10)){
            SimilarMovieSource(moviesRepository,movieId)
        }.flow.cachedIn(viewModelScope)
        return similarMoviesListPaging
    }

    suspend fun getMovieCast(movieId: Int?):MutableList<Cast>? {
        var castList: MutableList<Cast>? = null
        val result = moviesRepository.getMovieCast(movieId)
        try {
            progressBar.postValue(true)  //value = true
            when (result) {
                is Result.Success -> {
                    withContext(Dispatchers.Main){
                        progressBar.postValue(false)
                        castList = result.data.cast
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
        return castList
    }
    suspend fun getArabicMovieDetails(movieId: Int?):MovieItem? {
        var movieDetails: MovieItem? = null
        val result = moviesRepository.getArabicMovieDetails(movieId)
        try {
            progressBar.postValue(true)  //value = true
            when (result) {
                is Result.Success -> {
                    withContext(Dispatchers.Main){
                        progressBar.postValue(false)
                        movieDetails = result.data
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
        return movieDetails
    }

    fun addFavoriteMovie(movieItem: MovieItem){
        moviesRepository.addFavoriteMovie(movieItem)
    }
    fun removeMovieFromFavorites(movieItem: MovieItem) {
        moviesRepository.removeMovieFromFavorites(movieItem)
    }

    fun isMovieExist(movieItem: MovieItem): Boolean {
        val currentList = Gson().fromJson<MutableList<MovieItem>>(
            SharedPreference.getString(Constant.FAVORITE_LIST, ""),
            object : TypeToken<MutableList<MovieItem>>() {}.type
        )
        if (!currentList.isNullOrEmpty()) {
            return currentList.contains(movieItem)
        }
        return false
    }

    fun getCategoriesNames(genreIds: MutableList<Int?>?): String {
        val categoriesList = Gson().fromJson<CategoryResponse>(
            SharedPreference.getString(Constant.CATEGORIES_DATA, ""),
            object : TypeToken<CategoryResponse>() {}.type
        ).genres
        var categoriesFilter: List<String>? = null
        if (!genreIds.isNullOrEmpty()) {
            categoriesFilter = categoriesList.filter {
                genreIds.contains(it.id)
            }.map {
                it.name
            }
        }
        return categoriesFilter.toString().replace("[", "").replace("]", "")
    }

}