package com.example.ds_movies.ui.trendingActorList

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ds_movies.data.paging.TrendingActorsSource
import com.example.ds_movies.data.repositories.MoviesRepository
import com.example.ds_movies.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrendingActorsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
):BaseViewModel() {

    val trendingActorsListPaging = Pager(PagingConfig(10)){
        TrendingActorsSource(moviesRepository)
    }.flow.cachedIn(viewModelScope)
}