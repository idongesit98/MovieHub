package com.zseni.moviehub.data.remote

import com.zseni.moviehub.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    /**
     * Movies Api
     */
    @GET("movie/{category}")
    suspend fun getNowPlayingMovies(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en"
    ):MovieListDto

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") time: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en"
    ): MovieListDto

    @GET("movie/{category}")
    suspend fun getPopularMovies(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en"
    ): MovieListDto

    @GET("movie/{category}")
    suspend fun getTopRatedMovies(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en"
    ): MovieListDto


    @GET("movie/{category}")
    suspend fun getUpcomingMovies(
        @Path("category") category: String,
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en"
    ):MovieListDto

    @GET("movie/{movie_id}/{category}")
    suspend fun getRecommendedMovies(
        @Path("category") category: String,
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en"
    ): MovieListDto

    @GET("movie/{movie_id}/{category}")
    suspend fun getSimilarMovies(
        @Path("category") category: String,
        @Path("movie_id") filmId: Int,
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en"
    ): MovieListDto

    @GET("movie/{movie_id}/credits")//credits
    suspend fun getMovieCast(
        @Path("movie_id") filmId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): CastDto

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US"
    ): GenreListDto

    @GET("search/multi")
    suspend fun searchMovies(
        @Query("query") query:String,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = true,
        @Query("api_key") apikey: String = BuildConfig.API_KEY
    ): MovieListDto

    /**
     * TvShows Api
     */

    @GET("tv/{tv_id}/credits")
    suspend fun getTvShowCast(
        @Path("tv_id") filmId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): CastDto

    @GET("tv/{tv_id}/{category}")
    suspend fun getSimilarTvShows(
        @Path("category") category: String,
        @Path("tv_id") filmId: Int,
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US"
    ):MovieListDto

    @GET("trending/tv/{time_window}")
    suspend fun getTrendingTvSeries(
        @Path("time_window") time: String,
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US"
    ): MovieListDto

    @GET("tv/{category}")
    suspend fun getPopularTvShows(
        @Path("category") category: String,
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US"
    ): MovieListDto


    @GET("tv/{category}")
    suspend fun getTopRatedTvShows(
        @Path("category") category: String,
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US"
    ): MovieListDto

    @GET("tv/{category}")
    suspend fun getUpcomingTvSeries(
        @Path("category") category: String,
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en"
    ):MovieListDto

    @GET("tv/{tv_id}/{category}")
    suspend fun getRecommendedTvShows(
        @Path("category") category: String,
        @Path("tv_id") filmId: Int,
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US"
    ):MovieListDto

    @GET("tv/{category}")
    suspend fun getNowPlayingTvSeries(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en"
    ):MovieListDto

    @GET("{film_path}/{film_id}/reviews?")
    suspend fun getMovieReviews(
        @Path("film_id") filmId: Int,
        @Path("film_path") filmPath: String,
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US"
    ): ReviewDto

    @GET("genre/tv/list")
    suspend fun getTvShowGenres(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = "en-US"
    ): GenreListDto
}