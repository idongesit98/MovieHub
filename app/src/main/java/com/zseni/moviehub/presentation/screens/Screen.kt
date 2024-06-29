package com.zseni.moviehub.presentation.screens

sealed class Screen(val route:String) {
    data object Home:Screen("home")
    data object TvSeries:Screen("tvSeries")
    data object Details:Screen("details")
    data object Search:Screen("search")
    data object Splash:Screen("splash")
}