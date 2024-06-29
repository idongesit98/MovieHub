package com.zseni.moviehub.presentation.screens.homeScreen

sealed class UiEvent {
    data class Refresh(val category:String): UiEvent()
    //data class Paginate(val category: String): UiEvent()
}