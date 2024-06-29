package com.zseni.moviehub.presentation.screens.tvScreen

import com.zseni.moviehub.domain.model.Genre
import com.zseni.moviehub.domain.model.Movie

data class TvState(
    val isLoading:Boolean = false,
    val isRefreshing:Boolean = false,
    val upComingTvSeriesPage:Int = 1,
    val topRatedTvSeriesPage:Int = 1,
    val onTheAirTvSeriesPage:Int = 1,
    val popularTvSeriesPage:Int = 1,
    val nowPlayingTvSeriesPage:Int = 1,
    val trendingTvSeriesPage:Int = 1,
    val recommendedTvSeriesPage:Int = 1,
    val isCurrentScreen:Boolean = true,

    val upcomingTvSeries:List<Movie> = emptyList(),
    val topRatedTvSeries:List<Movie> = emptyList(),
    val airingTodayTvSeriesList:List<Movie> = emptyList(),
    val popularTvSeries:List<Movie> = emptyList(),
    val latestMoviesList:List<Movie> = emptyList(),
    val trendingTvSeries:List<Movie> = emptyList(),
    val nowPlayingTvSeries: List<Movie> = emptyList(),
    val recommendedTvSeries:List<Movie> = emptyList(),


    val moviesGenresList:List<Genre> = emptyList(),
    val tvGenresList:List<Genre> = emptyList(),
    val specialList: List<Movie> = emptyList()
)
