package com.example.ds_movies.ui.search

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.data.paging.MoviesSearchSource
import com.example.ds_movies.data.repositories.MoviesRepository
import com.example.ds_movies.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MoviesSearchViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) :BaseViewModel() {

    fun getMoviesSearchResult(query:String): Flow<PagingData<MovieItem>> {
        val moviesSearchListPaging = Pager(PagingConfig(10)){
            MoviesSearchSource(moviesRepository,query)
        }.flow.cachedIn(viewModelScope)
        return moviesSearchListPaging
    }
}