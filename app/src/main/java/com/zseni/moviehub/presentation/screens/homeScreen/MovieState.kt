package com.zseni.moviehub.presentation.screens.homeScreen

import com.zseni.moviehub.domain.model.Genre
import com.zseni.moviehub.domain.model.Movie

data class MovieState(
    val isLoading:Boolean = false,
    val isRefreshing:Boolean = false,
    val popularMovieListPage:Int = 1,
    val upComingMovieListPage:Int = 1,
    val topRatedTvSeriesPage:Int = 1,
    val topRatedMovieListPage:Int = 1,
    val onTheAirTvSeriesPage:Int = 1,
    val popularTvSeriesPage:Int = 1,
    val nowPlayingMoviesPage:Int = 1,
    val trendingAllPage:Int = 1,
    val recommendedPage:Int = 1,
    val isCurrentScreen:Boolean = true,


    val popularMoviesList:List<Movie> = emptyList(),
    val upcomingMovieList:List<Movie> = emptyList(),
    val topRatedTvSeriesList:List<Movie> = emptyList(),
    val topRatedTvMovieList: List<Movie> = emptyList(),
    val airingTodayTvSeriesList:List<Movie> = emptyList(),
    val popularTvSeriesList:List<Movie> = emptyList(),
    val latestMoviesList:List<Movie> = emptyList(),
    val trendingAllList:List<Movie> = emptyList(),
    val nowPlayingMoviesList: List<Movie> = emptyList(),


    val tvSeriesList:List<Movie> = emptyList(),
    val moviesGenresList:List<Genre> = emptyList(),
    val tvGenresList:List<Genre> = emptyList(),
    val recommendedMoviesList:List<Movie> = emptyList(),
    val specialList: List<Movie> = emptyList(),
    val topRatedAllList: List<Movie> = emptyList()

//    val moviesGenresList: List<Genre> = emptyList(),
//    val tvGenresList: List<Genre> = emptyList(),



)
