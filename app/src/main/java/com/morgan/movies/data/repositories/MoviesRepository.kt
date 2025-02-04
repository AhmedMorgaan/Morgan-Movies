package com.morgan.movies.data.repositories

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.morgan.movies.core.SharedPreference
import com.morgan.movies.core.utils.Constant.Companion.FAVORITE_LIST
import com.morgan.movies.data.api.MoviesApi
import com.morgan.movies.data.base.RetrofitExecutor
import com.morgan.movies.data.models.MovieItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val moviesApi :MoviesApi,
    private val retrofitExecutor: RetrofitExecutor
    ) {

    suspend fun getTrendingMovies (page:Int) = moviesApi.getTrendingMoviesPaging(page = page)
    suspend fun getPopularActorsPaging (page:Int) = moviesApi.getPopularActorsPaging(page = page)
    suspend fun getTopRatedMoviesPaging(page:Int) = moviesApi.getTopRatedMoviesPaging(page)
    suspend fun getPopularMovies (page:Int) = moviesApi.getPopularMovies(page)
    suspend fun getNowPlayingMovies (page:Int) = moviesApi.getNowPlayingMoviesPaging(page)
    suspend fun getUpComingMovies (page:Int) = moviesApi.getUpComingMoviesPaging(page)
    suspend fun getRecommendationMovies (movie_id: Int?, page:Int) = moviesApi.getRecommendationMovies(movie_id,page)
    suspend fun getMoviesSearchResult (query:String, page:Int) = moviesApi.getMoviesSearchResult(query,page)
    suspend fun getActorsSearchResult (query:String, page:Int) = moviesApi.getActorsSearchResult(query,page)

    suspend fun getActorMovies (actorId: Int?) = retrofitExecutor.makeRequest { moviesApi.getActorMovies(actorId) }
    suspend fun getActorMoviesPaging (actorId: Int?,page: Int) =  moviesApi.getActorMoviesPaging(actorId = actorId, page = page)
    suspend fun getActorDetails (actorId: Int?) = retrofitExecutor.makeRequest { moviesApi.getActorDetails(actorId) }
    suspend fun getArabicActorDetails (actorId: Int?) = retrofitExecutor.makeRequest { moviesApi.getArabicActorDetails(actorId) }
    suspend fun getMovieCast (movieId:Int?) = retrofitExecutor.makeRequest { moviesApi.getMovieCast(movieId) }
    suspend fun getMoviesCategory() = retrofitExecutor.makeRequest { moviesApi.getMoviesCategory() }
    suspend fun getMovieVideo(movieId: Int?) = retrofitExecutor.makeRequest { moviesApi.getMovieVideo(movieId) }
    suspend fun getArabicMovieDetails(movieId: Int?) = retrofitExecutor.makeRequest { moviesApi.getArabicMovieDetails(movieId) }


    fun addFavoriteMovie(movie: MovieItem) {
        val currentList = Gson().fromJson<MutableList<MovieItem>>(
            SharedPreference.getString(FAVORITE_LIST, ""),
            object : TypeToken<MutableList<MovieItem>>() {}.type
        )
        if (currentList.isNullOrEmpty()) {
            val list :MutableList<MovieItem> = mutableListOf()
            list.add(movie)
            SharedPreference.saveString(FAVORITE_LIST, Gson().toJson(list))
        } else if (!currentList.contains(movie)) {
            currentList.add(movie)
            SharedPreference.saveString(FAVORITE_LIST, Gson().toJson(currentList))
        }
    }
    fun removeMovieFromFavorites(movie: MovieItem){
        val currentList = Gson().fromJson<MutableList<MovieItem>>(
            SharedPreference.getString(FAVORITE_LIST,""),
            object : TypeToken<MutableList<MovieItem>>() {}.type)
        if (!currentList.isNullOrEmpty()){
            if (currentList.contains(movie)){
                currentList.remove(movie)
                SharedPreference.saveString(FAVORITE_LIST,Gson().toJson(currentList))
            }
        }
    }


}