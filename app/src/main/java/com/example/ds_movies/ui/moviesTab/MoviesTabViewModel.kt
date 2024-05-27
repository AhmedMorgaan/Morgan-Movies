package com.example.ds_movies.ui.moviesTab

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ds_movies.core.Result
import com.example.ds_movies.core.SharedPreference
import com.example.ds_movies.core.utils.Constant.Companion.CATEGORIES_DATA
import com.example.ds_movies.data.models.Genre
import com.example.ds_movies.data.paging.NowPlayingMovieSource
import com.example.ds_movies.data.paging.PopularMovieSource
import com.example.ds_movies.data.paging.TopRatedMovieSource
import com.example.ds_movies.data.paging.TrendingMovieSource
import com.example.ds_movies.data.paging.UpComingMovieSource
import com.example.ds_movies.data.repositories.MoviesRepository
import com.example.ds_movies.ui.base.BaseViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesTabViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
):BaseViewModel() {

    private val _genresList = MutableLiveData<MutableList<Genre>>()
    val genresList = _genresList

    val topRatedMoviesListPaging = Pager(PagingConfig(10)){
        TopRatedMovieSource(moviesRepository)
    }.flow.cachedIn(viewModelScope)

    val popularMoviesListPaging = Pager(PagingConfig(10)){
        PopularMovieSource(moviesRepository)
    }.flow.cachedIn(viewModelScope)

    val nowPlayingMoviesListPaging = Pager(PagingConfig(10)){
        NowPlayingMovieSource(moviesRepository)
    }.flow.cachedIn(viewModelScope)

    val upComingMoviesListPaging = Pager(PagingConfig(10)){
        UpComingMovieSource(moviesRepository)
    }.flow.cachedIn(viewModelScope)

    val trendingMoviesListPaging = Pager(PagingConfig(10)){
        TrendingMovieSource(moviesRepository)
    }.flow.cachedIn(viewModelScope)


     fun getMoviesCategories(){
        viewModelScope.launch {
            progressBar.postValue(true)
           val result =  moviesRepository.getMoviesCategory()
            when (result) {
                is Result.Success -> {
                    progressBar.postValue(false)
                    _genresList.value = result.data.genres
                    SharedPreference.saveString(CATEGORIES_DATA, Gson().toJson(result.data))
                }
                is Result.Error -> {
                    progressBar.postValue(false)
                    showMessage.value = result.exception.message
                }
                is Result.Loading -> {
                    progressBar.postValue(true)
                }
            }
        }
    }

//    fun getTopRatedMovies(genreId:Int?){
//        viewModelScope.launch {
//            progressBar.value = true
//            val result =  moviesRepository.getTopRatedMovies()
//            when (result) {
//                is Result.Success -> {
//                    progressBar.value = false
//                    val results = result.data.results
//
//                    if (genreId !=null){
//                        val listFilter = results?.filter {
//                            it?.genreIds!!.contains(genreId)
//                        }?.toMutableList()
//                        _topRatedMoviesList.value = listFilter
//                        Log.e("filter", "getTopRatedMovies: $listFilter", )
//                    }
//                    else{
//                        _topRatedMoviesList.value = result.data.results
//                        Log.e("topMovies", "getTopRatedMovies: ${result.data.results}", )
//                    }
//                }
//                is Result.Error -> {
//                    progressBar.value = false
//                    showMessage.value = result.exception.message
//                }
//                is Result.Loading -> {
//                    progressBar.value = true
//                }
//            }
//        }
//    }
}
