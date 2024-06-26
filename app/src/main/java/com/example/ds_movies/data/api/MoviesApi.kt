package com.example.ds_movies.data.api

import com.example.ds_movies.data.models.CastResponse
import com.example.ds_movies.data.models.CategoryResponse
import com.example.ds_movies.data.models.MovieItem
import com.example.ds_movies.data.models.MoviesResponse
import com.example.ds_movies.data.models.VideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MoviesApi {

    //BASE_URL= "https://api.themoviedb.org/3/"

    @GET("movie/popular")
    suspend fun getPopularMovies (
        @Query("page") page:Int,
        @Query("language") language:String = "en"
    ) : Response<MoviesResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMoviesPaging(
        @Query("page") page:Int,
        @Query("language") language:String = "en"
    ) : Response<MoviesResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMoviesPaging(
        @Query("page") page:Int,
        @Query("language") language:String = "en"
    ) : Response<MoviesResponse>

    @GET("movie/upcoming")
    suspend fun getUpComingMoviesPaging(
        @Query("page") page:Int,
        @Query("language") language:String = "en"
    ) : Response<MoviesResponse>

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMoviesPaging(
        @Path("time_window") time_window:String = "day",
        @Query("page") page:Int,
        @Query("language") language:String = "en"
    ) : Response<MoviesResponse>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCast(
        @Path("movie_id") movie_id:Int?,
        @Query("language") language:String = "en"
    ) : Response<CastResponse>


    @GET("movie/{movie_id}")
    suspend fun getArabicMovieDetails(
        @Path("movie_id") movie_id:Int?,
        @Query("language") language:String = "ar"
    ) : Response<MovieItem>

    @GET("genre/movie/list")
    suspend fun getMoviesCategory() : Response<CategoryResponse>

    @GET("discover/movie")
    suspend fun getMoviesWithGenres(
        @Query("with_genres") genreId:Int
    ): Response<MoviesResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideo(
        @Path("movie_id") movie_id: Int?
    ): Response<VideoResponse>

}