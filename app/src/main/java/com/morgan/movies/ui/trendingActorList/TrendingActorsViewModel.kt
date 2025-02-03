package com.morgan.movies.ui.trendingActorList

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.morgan.movies.data.paging.TrendingActorsSource
import com.morgan.movies.data.repositories.MoviesRepository
import com.morgan.movies.ui.base.BaseViewModel
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