package com.zseni.moviehub.presentation.screens.movieDetails

import com.zseni.moviehub.domain.model.Movie

data class DetailsState(
    val isLoading:Boolean = false,
    val movie: Movie? = null,
    val tv: Movie? = null
)
