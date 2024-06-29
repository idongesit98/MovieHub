package com.zseni.moviehub.presentation.screens.searchScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.zseni.moviehub.presentation.screens.searchScreen.components.SearchMovieItem
import com.zseni.moviehub.presentation.screens.searchScreen.components.SearchScreenBar
import com.zseni.moviehub.presentation.screens.searchScreen.components.SearchUiEvents
import com.zseni.moviehub.presentation.viewModels.SearchViewModel
import com.zseni.moviehub.presentation.screens.homeScreen.MovieState
import com.zseni.moviehub.R
import com.zseni.moviehub.util.ReverseLottie
import kotlin.math.roundToInt

@Composable
fun SearchScreen(
    navController: NavController,
    movieState: MovieState
){
    val searchViewModel = hiltViewModel<SearchViewModel>()
    val searchScreenState = searchViewModel.searchState.collectAsState().value
    val toolBarHeightPx = with(LocalDensity.current){74.dp.roundToPx().toFloat()}
    val toolbarOffsetHeightPx = remember{ mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember{
        object :NestedScrollConnection{
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.floatValue + delta
                toolbarOffsetHeightPx.floatValue = newOffset.coerceIn(-toolBarHeightPx,0f)
                return Offset.Zero
            }
        }
    }

    Column(modifier = Modifier
        .padding(top = 10.dp)
        .fillMaxSize()
    ) {
        SearchScreenBar(
            toolbarOffsetHeightPx = toolbarOffsetHeightPx.floatValue.roundToInt(),
            searchState = searchScreenState
        ) {
            searchViewModel.onEvent(SearchUiEvents.OnSearchQueryChanged(it))
        }

        if (searchScreenState.searchList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .nestedScroll(nestedScrollConnection),
                contentAlignment = Alignment.Center
            ) {
                ReverseLottie(lottieFile = R.raw.searching)
            }
        }else {
            LazyVerticalGrid(
                contentPadding = PaddingValues(top = 10.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(searchScreenState.searchList.size) {
                    SearchMovieItem(
                        movie = searchScreenState.searchList[it],
                        navController = navController,
                        movieState = movieState,
                        onEvent = searchViewModel::onEvent
                    )
                    if (it >= searchViewModel.searchState.value.searchList.size - 1 && !movieState.isLoading) {
                        searchViewModel.onEvent(SearchUiEvents.OnPaginate("searchScreen"))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSearchScreen(){
    val navController = rememberNavController()
    SearchScreen(
        navController = navController,
        movieState = MovieState()
    )
}