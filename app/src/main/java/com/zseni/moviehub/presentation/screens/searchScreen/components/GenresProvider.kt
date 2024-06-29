package com.zseni.moviehub.presentation.screens.searchScreen.components

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.zseni.moviehub.presentation.viewModels.MovieViewModel

@Composable
fun genresProvider(
    genre_ids: List<Int>,
    //allGenres: List<Genre>
    viewModel: MovieViewModel = hiltViewModel()
): String {
    val allGenres = viewModel.movieGenres
    return allGenres
        .filter { it.id in genre_ids }
        .joinToString(separator = " - ") { it.name }
}
/**
 * This code uses filter to select only genres with ID present in 'genre_ids'
 * and 'joinToString' to concatenate their names with a separator
 */