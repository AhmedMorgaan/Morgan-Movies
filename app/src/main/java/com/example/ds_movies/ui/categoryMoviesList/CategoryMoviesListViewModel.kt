package com.example.ds_movies.ui.categoryMoviesList

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ds_movies.data.paging.NowPlayingMovieSource
import com.example.ds_movies.data.paging.PopularMovieSource
import com.example.ds_movies.data.paging.TopRatedMovieSource
import com.example.ds_movies.data.paging.TrendingMovieSource
import com.example.ds_movies.data.paging.UpComingMovieSource
import com.example.ds_movies.data.repositories.MoviesRepository
import com.example.ds_movies.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryMoviesListViewModel @Inject constructor(
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

}