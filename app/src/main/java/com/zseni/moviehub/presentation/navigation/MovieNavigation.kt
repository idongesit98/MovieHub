package com.zseni.moviehub.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zseni.moviehub.domain.model.Movie
import com.zseni.moviehub.presentation.screens.Screen
import com.zseni.moviehub.presentation.screens.homeScreen.Home
import com.zseni.moviehub.presentation.screens.homeScreen.MovieState
import com.zseni.moviehub.presentation.screens.homeScreen.SplashScreen
import com.zseni.moviehub.presentation.screens.movieDetails.DetailScreen
import com.zseni.moviehub.presentation.screens.searchScreen.SearchScreen
import com.zseni.moviehub.presentation.screens.tvScreen.TvMovies
import com.zseni.moviehub.util.MovieType

@Composable
fun MovieNavigation(movieState: MovieState) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route){
            SplashScreen(navController)
        }
        composable(Screen.Home.route){
            Home(navController = navController, movie = sampleMovie)
        }

        composable(Screen.TvSeries.route){
            TvMovies(navController = navController)
        }

        composable(Screen.Details.route + "/{movieId}",
            arguments = listOf(
                navArgument("movieId"){type = NavType.IntType}
            )
        ){
            DetailScreen(currentMovie = sampleMovie, selectedMovieType = MovieType.Movie)
        }
        composable(Screen.Search.route){
            SearchScreen(navController = navController, movieState = movieState)
        }
    }
}
val sampleMovie = Movie(
    adult = false,
    backdrop_path = "sample_backdrop_path",
    genre_ids = listOf(1, 2, 3),
    id = 123,
    first_air_date = "2020-01-01",
    origin_country = listOf("US"),
    original_language = "en",
    original_title = "Sample Title",
    original_name = "Sample Name",
    overview = "This is a sample overview of the movie.",
    mediaType = "movie",
    popularity = 10.0,
    poster_path = "sample_poster_path",
    release_date = "2020-01-01",
    title = "Sample Title",
    video = false,
    vote_average = 7.5,
    vote_count = 100,
    category = "Sample Category",
    name = "Sample Name",
    runtime = 120,
    status = "Released",
    tagline = "This is a sample tagline.",
    videos = listOf("sample_video_1", "sample_video_2"),
    similarMediaList = listOf(4, 5, 6)
)
