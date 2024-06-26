package com.example.ds_movies.data.repositories

import com.example.ds_movies.data.api.MoviesApi
import com.example.ds_movies.data.base.RetrofitExecutor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val moviesApi :MoviesApi,
    private val retrofitExecutor: RetrofitExecutor
    ) {
    suspend fun getMoviesMainCategory() = moviesApi.getMoviesCategory()
    suspend fun getMoviesWithGenres(genreId:Int) = moviesApi.getMoviesWithGenres(genreId)

    suspend fun getTrendingMovies (page:Int) = moviesApi.getTrendingMoviesPaging(page = page)
    suspend fun getTopRatedMoviesPaging(page:Int) = moviesApi.getTopRatedMoviesPaging(page)
    suspend fun getPopularMovies (page:Int) = moviesApi.getPopularMovies(page)
    suspend fun getNowPlayingMovies (page:Int) = moviesApi.getNowPlayingMoviesPaging(page)
    suspend fun getUpComingMovies (page:Int) = moviesApi.getUpComingMoviesPaging(page)
    suspend fun getMovieCast (movieId:Int?) = retrofitExecutor.makeRequest { moviesApi.getMovieCast(movieId) }
    suspend fun getMoviesCategory() = retrofitExecutor.makeRequest { moviesApi.getMoviesCategory() }
    suspend fun getMovieVideo(movieId: Int?) = retrofitExecutor.makeRequest { moviesApi.getMovieVideo(movieId) }
    suspend fun getArabicMovieDetails(movieId: Int?) = retrofitExecutor.makeRequest { moviesApi.getArabicMovieDetails(movieId) }

}