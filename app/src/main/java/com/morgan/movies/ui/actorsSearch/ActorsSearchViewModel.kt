package com.morgan.movies.ui.actorsSearch

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.morgan.movies.data.models.ActorItem
import com.morgan.movies.data.paging.ActorsSearchSource
import com.morgan.movies.data.repositories.MoviesRepository
import com.morgan.movies.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ActorsSearchViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) :BaseViewModel() {

    fun getActorsSearchResult(query:String): Flow<PagingData<ActorItem>> {
        val actorsSearchListPaging = Pager(PagingConfig(10)){
            ActorsSearchSource(moviesRepository,query)
        }.flow.cachedIn(viewModelScope)
        return actorsSearchListPaging
    }
}