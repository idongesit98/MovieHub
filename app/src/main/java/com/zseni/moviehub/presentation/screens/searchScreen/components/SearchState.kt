package com.zseni.moviehub.presentation.screens.searchScreen.components

import com.zseni.moviehub.domain.model.Movie

data class SearchState(
    val isLoading:Boolean = false,
    val searchPage:Int = 1,
    val isRefreshing:Boolean = false,
    val searchQuery:String = "",
    val searchList: List<Movie> = emptyList()
)
