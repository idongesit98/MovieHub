package com.zseni.moviehub.presentation.screens.searchScreen.components

import com.zseni.moviehub.domain.model.Movie

sealed class SearchUiEvents {
    data class Refresh(val type:String): SearchUiEvents()
    data class OnPaginate(val type:String): SearchUiEvents()
    data class OnSearchQueryChanged(val query:String): SearchUiEvents()
    data class OnSearchItemClicked(val movie: Movie): SearchUiEvents()
}