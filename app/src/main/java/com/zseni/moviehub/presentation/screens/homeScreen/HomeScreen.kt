package com.zseni.moviehub.presentation.screens.homeScreen

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Person
import androidx.compose.material.icons.twotone.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.zseni.moviehub.R
import com.zseni.moviehub.domain.model.Movie
import com.zseni.moviehub.presentation.component.MovieItem
import com.zseni.moviehub.presentation.component.ShowAboutCategory
import com.zseni.moviehub.presentation.screens.Screen
import com.zseni.moviehub.presentation.screens.components.SelectableGenre
import com.zseni.moviehub.presentation.viewModels.MovieViewModel
import com.zseni.moviehub.presentation.viewModels.WatchViewModel
import com.zseni.moviehub.ui.theme.AppColour
import com.zseni.moviehub.ui.theme.abeezeeFont
import com.zseni.moviehub.ui.theme.akayaFont
import com.zseni.moviehub.util.MovieType

@Composable
fun Home(navController: NavController,movie: Movie) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        TopHeaderBar(navController = navController)
        ScrollingTabs(navController = navController, currentMovie = movie)
    }
}

@Composable
fun TopHeaderBar(
    viewModel: MovieViewModel = hiltViewModel(),
    navController:NavController
) {
    val movieTypes = listOf(MovieType.Movie, MovieType.TvSeries)
    val selectedMovieType by viewModel.selectedMovieType.collectAsState()
    val styledText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 42.sp, brush = Brush.horizontalGradient(
                    listOf(
                        Color.Black,
                        Color.LightGray, Color.Magenta, Color.Gray
                    )
                )
            )
        ) {
            append(stringResource(R.string.movie_hub))
        }
    }
    val animOffset = animateDpAsState(
        targetValue = when (movieTypes.indexOf(selectedMovieType)) {
            0 -> 10.dp
            else -> 10.dp
        },
        label = "",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy
        )
    )
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.Center,
            content = {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ }
                        ) {
                            Icon(
                                imageVector = Icons.TwoTone.Person,
                                contentDescription = ""
                            )
                        }
                        Text(
                            modifier = Modifier
                                .padding(start = 20.dp, end = 10.dp),
                            text = styledText
                        )
                        IconButton(
                            onClick = { navController.navigate(Screen.Search.route) },
                            modifier = Modifier
                                .padding(start = 20.dp, end = 5.dp)
                                .size(70.dp)
                        ) {
                            Icon(
                                imageVector = Icons.TwoTone.Search,
                                contentDescription = "Search",
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = animOffset.value)
                    ) {
                        movieTypes.forEachIndexed { index, movieType ->
                            Text(
                                text = if (movieType == MovieType.Movie) "Movies" else "TvSeries",
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clickable(
                                        interactionSource = remember {
                                            MutableInteractionSource()
                                        },
                                        indication = null
                                    ) {
                                        if (movieType == MovieType.TvSeries) {
                                            navController.navigate(Screen.TvSeries.route)
                                        }
                                    },
                                fontWeight = if (selectedMovieType == movieTypes[index]) FontWeight.Bold else FontWeight.Light,
                                fontSize = if (selectedMovieType == movieTypes[index]) 24.sp else 16.sp,
                                color = if (selectedMovieType == movieTypes[index])
                                    AppColour else Color.LightGray.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ScrollingTabs(
    viewModel: MovieViewModel = hiltViewModel(),
    watchViewModel: WatchViewModel = hiltViewModel(),
    navController: NavController,
    currentMovie: Movie
) {
    val moviesListState by viewModel.movieListState.collectAsState()
    val topRatedMovies = moviesListState.topRatedTvMovieList
    val trendingMovies = moviesListState.trendingAllList
    Log.d("ScrollingTabs", "Trending Movies List: ${trendingMovies.size}")
    val recommended = moviesListState.recommendedMoviesList
    Log.d("ScrollingRecom", "Trending Movies List: ${recommended.size}")
    val popularMovie = moviesListState.popularMoviesList
    val upcomingMovies = moviesListState.upcomingMovieList
    val nowPlayingMovies = moviesListState.nowPlayingMoviesList
    val watchList = watchViewModel.watchList.value.collectAsState(initial = emptyList())
    val movie by remember {
        mutableStateOf(currentMovie)
    }

    LaunchedEffect(key1 = movie) {
        viewModel.getRecommendedMovies(true, movieId = movie.id)
        Log.d("Check cast","Cast Api:${viewModel.getRecommendedMovies(true, movieId = movie.id)}")
    }

    val listState:LazyListState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .fillMaxSize()
    ) {
        item {
            Text(
                text = stringResource(R.string.genres),
                modifier = Modifier.padding(start = 4.dp, end = 5.dp),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = abeezeeFont
            )
        }
        item {
            val genres = viewModel.movieGenres
            LazyRow(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            ) {
               items(count = genres.size){
                   SelectableGenre(
                       genre = genres[it].name,
                       selected = genres[it].name == viewModel.selectedGenre.value.name,
                       onClick = {
                           if (viewModel.selectedGenre.value.name != genres[it].name){
                               viewModel.selectedGenre.value = genres[it]
                               viewModel.filterBySelectedGenre(genre = genres[it])
                           }
                       }
                   )
               }
            }
        }

        item {
            Text(
                text = stringResource(R.string.now_playing),
                modifier = Modifier
                    .padding(start = 4.dp, top = 8.dp),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = akayaFont
            )
        }
        item{
            MovieItem(
                movieList = nowPlayingMovies,
                clickable = {},
                navController = navController
            )
        }

        item{
            Text(
                text = stringResource(R.string.top_rated),
                modifier = Modifier
                    .padding(start = 4.dp, top = 5.dp),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = akayaFont
            )
        }
        item{
            MovieItem(
                movieList = topRatedMovies,
                clickable = {},
                navController = navController
            )
        }
        item {
            Text(
                text = stringResource(R.string.popular),
                modifier = Modifier
                    .padding(start = 4.dp, top = 8.dp),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = akayaFont
            )
        }
        item{
            MovieItem(
                movieList = popularMovie,
                clickable = {},
                navController = navController
            )
        }
        item {
            Text(
                text = stringResource(R.string.trending),
                modifier = Modifier
                    .padding(start = 4.dp, top = 8.dp),
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
                fontFamily = akayaFont
            )
        }
        item{
            MovieItem(
                movieList = trendingMovies,
                clickable = {},
                navController = navController
            )
        }
        if (recommended.isNotEmpty()){
            item {
                ShowAboutCategory(
                    name = "for you",
                    description = "Recommendations based on you"
                )
            }
        item{
            MovieItem(
                movieList = recommended,
                clickable = {},
                navController = navController
            )
        }
        }

        item {
            Text(
                text = stringResource(R.string.upcoming),
                modifier = Modifier
                    .padding(start = 4.dp, top = 8.dp),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = akayaFont
            )
        }
        item{
            MovieItem(
                movieList = upcomingMovies,
                clickable = {},
                navController = navController
            )
        }
    }
}


@Preview
@Composable
private fun PreviewTopBar() {
    val navController = rememberNavController()
    TopHeaderBar(navController = navController)
}
