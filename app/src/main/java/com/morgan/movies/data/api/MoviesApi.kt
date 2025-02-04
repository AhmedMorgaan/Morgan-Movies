package com.morgan.movies.data.api

import com.morgan.movies.data.models.ActorDetailsResponse
import com.morgan.movies.data.models.ActorMoviesResponse
import com.morgan.movies.data.models.CastResponse
import com.morgan.movies.data.models.CategoryResponse
import com.morgan.movies.data.models.MovieItem
import com.morgan.movies.data.models.MoviesResponse
import com.morgan.movies.data.models.PopularActorsResponse
import com.morgan.movies.data.models.VideoResponse
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
        @Path("movie_id") movieId:Int?,
        @Query("language") language:String = "en"
    ) : Response<CastResponse>


    @GET("movie/{movie_id}")
    suspend fun getArabicMovieDetails(
        @Path("movie_id") movieId:Int?,
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
        @Path("movie_id") movieId: Int?
    ): Response<VideoResponse>

    @GET("search/movie")
    suspend fun getMoviesSearchResult(
        @Query("query") query: String,
        @Query("page") page:Int
    ): Response<MoviesResponse>

    @GET("search/person")
    suspend fun getActorsSearchResult(
        @Query("query") query: String,
        @Query("page") page:Int
    ): Response<PopularActorsResponse>

    @GET("movie/{movie_id}/recommendations")
    suspend fun getRecommendationMovies(
        @Path("movie_id") movieId: Int?,
        @Query("page") page:Int
    ): Response<MoviesResponse>

    @GET("person/popular")
    suspend fun getPopularActorsPaging(
        @Query("page") page:Int,
        @Query("language") language:String = "en"
    ) : Response<PopularActorsResponse>

    @GET("person/{person_id}")
    suspend fun getActorDetails(
        @Path("person_id") actorId: Int?,
        @Query("language") language:String = "en"
    ): Response<ActorDetailsResponse>

    @GET("person/{person_id}")
    suspend fun getArabicActorDetails(
        @Path("person_id") actorId: Int?,
        @Query("language") language:String = "ar"
    ): Response<ActorDetailsResponse>

    @GET("person/{person_id}/movie_credits")
    suspend fun getActorMovies(
        @Path("person_id") actorId: Int?,
        @Query("language") language:String = "en",
    ): Response<ActorMoviesResponse>

    @GET("person/{person_id}/movie_credits")
    suspend fun getActorMoviesPaging(
        @Path("person_id") actorId: Int?,
        @Query("language") language:String = "en",
        @Query("page") page:Int,
    ): Response<ActorMoviesResponse>

}