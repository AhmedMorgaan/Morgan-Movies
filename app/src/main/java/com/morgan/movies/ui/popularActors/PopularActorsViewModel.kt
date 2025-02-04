package com.morgan.movies.ui.popularActors

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.morgan.movies.data.paging.PopularActorsSource
import com.morgan.movies.data.repositories.MoviesRepository
import com.morgan.movies.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PopularActorsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
):BaseViewModel() {

    val popularActorsListPaging = Pager(PagingConfig(10)){
        PopularActorsSource(moviesRepository)
    }.flow.cachedIn(viewModelScope)
}