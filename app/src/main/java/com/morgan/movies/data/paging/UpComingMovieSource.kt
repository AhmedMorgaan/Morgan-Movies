package com.morgan.movies.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.morgan.movies.data.models.MovieItem
import com.morgan.movies.data.repositories.MoviesRepository
import retrofit2.HttpException

class UpComingMovieSource(
    private val moviesRepository: MoviesRepository) : PagingSource<Int, MovieItem>() {

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        return  try {
            val currentPage = params.key ?:1
            val response = moviesRepository.getUpComingMovies(currentPage)
            val data = response.body()?.results?.toList()
            val responseData = mutableListOf<MovieItem>()
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