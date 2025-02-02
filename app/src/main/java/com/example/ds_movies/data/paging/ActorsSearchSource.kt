package com.example.ds_movies.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ds_movies.data.models.ActorItem
import com.example.ds_movies.data.repositories.MoviesRepository
import retrofit2.HttpException

class ActorsSearchSource(
    private val moviesRepository: MoviesRepository,
    private val query : String
    ) : PagingSource<Int, ActorItem>() {

    override fun getRefreshKey(state: PagingState<Int, ActorItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ActorItem> {
        return  try {
            val currentPage = params.key ?:1
            val response = moviesRepository.getActorsSearchResult(query,currentPage)
            val data = response.body()?.results?.toList()
            val responseData = mutableListOf<ActorItem>()
            responseData.addAll(data!!.toList())

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = if (currentPage == 30)null else currentPage.plus(1)
            )
        }
        catch (e:Exception){
            LoadResult.Error(e)
        }
        catch (httpE : HttpException){
            LoadResult.Error(httpE)
        }
    }

}