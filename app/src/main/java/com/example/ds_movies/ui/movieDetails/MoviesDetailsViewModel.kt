package com.example.ds_movies.ui.movieDetails

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ds_movies.core.Result
import com.example.ds_movies.data.models.Cast
import com.example.ds_movies.data.paging.NowPlayingMovieSource
import com.example.ds_movies.data.paging.PopularMovieSource
import com.example.ds_movies.data.paging.TopRatedMovieSource
import com.example.ds_movies.data.paging.TrendingMovieSource
import com.example.ds_movies.data.paging.UpComingMovieSource
import com.example.ds_movies.data.repositories.MoviesRepository
import com.example.ds_movies.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MoviesDetailsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : BaseViewModel() {

    val topRatedMoviesListPaging = Pager(PagingConfig(20)){
        TopRatedMovieSource(moviesRepository)
    }.flow.cachedIn(viewModelScope)

    val popularMoviesListPaging = Pager(PagingConfig(20)){
        PopularMovieSource(moviesRepository)
    }.flow.cachedIn(viewModelScope)

    val nowPlayingMoviesListPaging = Pager(PagingConfig(20)){
        NowPlayingMovieSource(moviesRepository)
    }.flow.cachedIn(viewModelScope)

    val upComingMoviesListPaging = Pager(PagingConfig(20)){
        UpComingMovieSource(moviesRepository)
    }.flow.cachedIn(viewModelScope)

    val trendingMoviesListPaging = Pager(PagingConfig(20)){
        TrendingMovieSource(moviesRepository)
    }.flow.cachedIn(viewModelScope)



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

//    fun getResult() {
//        GlobalScope.launch(Dispatchers.IO) {
//
//            try {
//                val response = moviesRepository.getPopularMovies(1)
//               //val response = moviesRepository.getPopularMovies(Constant.apiKay)
//               //val response = ApiManager.getInstance().getPopularMovies(Constant.apiKay)
//                if (response.isSuccessful) {
//                    withContext(Dispatchers.Main) {
//                        progressBar.value = true
//                        resultLiveData.value = response.body()?.results
//                    }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    progressBar.value = true
//                    showMessage.value = e.localizedMessage
//                }
//            }
//
//        }
//
//    }

}