package com.zseni.moviehub.presentation.screens.tvScreen

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
import com.zseni.moviehub.presentation.component.MovieItem
import com.zseni.moviehub.presentation.screens.components.SelectableGenre
import com.zseni.moviehub.presentation.viewModels.TvSeriesViewModel
import com.zseni.moviehub.presentation.viewModels.WatchViewModel
import com.zseni.moviehub.ui.theme.AppColour
import com.zseni.moviehub.ui.theme.akayaFont
import com.zseni.moviehub.util.MovieType

@Composable
fun TvMovies(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        TopHeaderBar()
        ScrollingTabs(navController = navController)
    }
}

@Composable
fun TopHeaderBar(
    tvViewModel: TvSeriesViewModel = hiltViewModel()
) {
    val tvTypes = listOf(MovieType.Movie, MovieType.TvSeries)
    val selectedTvType by tvViewModel.selectedTvType.collectAsState()
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
        targetValue = when (tvTypes.indexOf(selectedTvType)) {
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
                            onClick = { /*TODO*/ },
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
                        tvTypes.forEachIndexed { index, movieType ->
                            Text(
                                text = if (movieType == MovieType.Movie) "Movies" else "TvSeries",
                                modifier = Modifier
                                    .padding(5.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        if (selectedTvType != tvTypes[index]) {
                                            tvViewModel.getTvGenre()
                                            tvViewModel.refreshAll()
                                        }

                                    },
                                fontWeight = if (selectedTvType == tvTypes[index]) FontWeight.Bold else FontWeight.Light,
                                fontSize = if (selectedTvType == tvTypes[index]) 24.sp else 16.sp,
                                color = if (selectedTvType == tvTypes[index])
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
    tvViewModel: TvSeriesViewModel = hiltViewModel(),
    watchViewModel:WatchViewModel = hiltViewModel(),
    navController: NavController
) {
    val tvListState by tvViewModel.tvState.collectAsState()
    val topRatedSeries = tvListState.topRatedTvSeries
    val trendingSeries = tvListState.trendingTvSeries
    Log.d("ScrollingTabs", "Trending Movies List: ${trendingSeries.size}")
    val recommendedSeries = tvListState.recommendedTvSeries
    val popularSeries = tvListState.popularTvSeries
    val upcomingSeries = tvListState.upcomingTvSeries
    val nowPlayingSeries = tvListState.nowPlayingTvSeries
    val watchMovies = watchViewModel.watchList.value.collectAsState(initial = emptyList())

    LaunchedEffect(key1 = watchMovies.value.size) {
        if (watchMovies.value.isNotEmpty()){
            tvViewModel.randomMovieId =
                watchMovies.value[(0.. watchMovies.value.lastIndex).random()].mediaId
            if (recommendedSeries.size == 0){
                tvViewModel.getRecommendedSeries(true, movieId = tvViewModel.randomMovieId!!)
            }
        }

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
                text = stringResource(id = R.string.genres),
                modifier = Modifier.padding(all = 4.dp),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = akayaFont
            )
        }
        item {
            val genres = tvViewModel.tvGenres
            LazyRow(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            ) {
               items(count = genres.size){
                   SelectableGenre(
                       genre = genres[it].name,
                       selected = genres[it].name == tvViewModel.selectedGenre.value.name,
                       onClick = {
                           if (tvViewModel.selectedGenre.value.name != genres[it].name){
                               tvViewModel.selectedGenre.value = genres[it]
                               tvViewModel.filterBySelectedGenre(genre = genres[it])
                           }
                       }
                   )
               }
            }
        }

        item {
            Text(
                text = stringResource(id = R.string.now_playing),
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        item{
            MovieItem(
                movieList = nowPlayingSeries,
                clickable = {},
                navController = navController
            )
        }

        item{
            Text(
                text = stringResource(id = R.string.top_rated),
                modifier = Modifier
                    .padding(start = 8.dp, top = 5.dp),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
        }
        item{
            MovieItem(
                movieList = topRatedSeries,
                clickable = {},
                navController = navController
            )
        }
        item {
            Text(
                text = stringResource(id = R.string.popular),
                modifier = Modifier
                    .padding(start = 4.dp, top = 8.dp),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        item{
            MovieItem(
                movieList = popularSeries,
                clickable = {},
                navController = navController
            )
        }
        item {
            Text(
                text = stringResource(id = R.string.trending),
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp),
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
            )
        }
        item{
            MovieItem(
                movieList = trendingSeries,
                clickable = {},
                navController = navController
            )
        }
        item {
            Text(
                text = stringResource(id = R.string.upcoming),
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        item{
            MovieItem(
                movieList = upcomingSeries,
                clickable = {},
                navController = navController
            )
        }
    }
}


@Preview
@Composable
private fun PreviewHome() {
    val navController = rememberNavController()
    TvMovies(navController = navController)
}

@Preview
@Composable
private fun PreviewTopBar() {
    TopHeaderBar()
}