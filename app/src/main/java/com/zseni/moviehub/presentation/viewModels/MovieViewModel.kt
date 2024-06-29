package com.zseni.moviehub.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zseni.moviehub.domain.model.Genre
import com.zseni.moviehub.domain.repository.GenreRepository
import com.zseni.moviehub.domain.repository.MovieRepository
import com.zseni.moviehub.domain.repository.NowPlayingRepo
import com.zseni.moviehub.domain.repository.PopularRepository
import com.zseni.moviehub.domain.repository.RecomRepository
import com.zseni.moviehub.domain.repository.SimilarRepos
import com.zseni.moviehub.domain.repository.TopRatedRepo
import com.zseni.moviehub.domain.repository.UpcomingRepository
import com.zseni.moviehub.presentation.screens.homeScreen.MovieState
import com.zseni.moviehub.util.MovieType
import com.zseni.moviehub.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val upcomingRepo: UpcomingRepository,
    private val nowPlayingRepo: NowPlayingRepo,
    private val topRatedRepo: TopRatedRepo,
    private val popularRepository: PopularRepository,
    private val recomRepository: RecomRepository,
    private val similarRepos: SimilarRepos,
    private val genreRepository: GenreRepository
) : ViewModel() {
    private val TAG = MovieViewModel::class.simpleName
    private var _movieGenres = mutableStateListOf(Genre(null, "All"))
    val movieGenres: SnapshotStateList<Genre> = _movieGenres

    var selectedGenre: MutableState<Genre> = mutableStateOf(Genre(null, "All"))
    private val _selectedMovieType = MutableStateFlow(MovieType.Movie)
    var selectedMovieType: StateFlow<MovieType> = _selectedMovieType.asStateFlow()


    private val _movieState = MutableStateFlow(MovieState())
    val movieListState:StateFlow<MovieState> = _movieState.asStateFlow()
    var randomMovieId:Int? = null

    init {
        refreshAll()
    }

    fun refreshAll(
        genreId: Int? = selectedGenre.value.id,
        movieType: MovieType = selectedMovieType.value
    ){
        if (movieGenres.size == 1){
            getMovieGenre(selectedMovieType.value)
        }
        if (genreId == null){
            selectedGenre.value = Genre(null,"All")
        }
        getTrendingAll(true, isRefresh = true,genreId)
        getUpcomingMovies(true, isRefresh = true,genreId)
        getPopularMovies(true, isRefresh = true,genreId)
        getNowPlayingMovies(true, isRefresh = true, genreId)
        getTopRatedMovie(true,isRefresh = true,genreId)
    }

    fun filterBySelectedGenre(genre: Genre) {
        selectedGenre.value = genre
        refreshAll(genre.id)
    }

    fun getMovieGenre(movieType: MovieType = _selectedMovieType.value) {
        viewModelScope.launch {
            val defaultGenre = Genre(null, "All")
            when (val results = genreRepository.getGenre(movieType)) {
                is Resource.Success -> {
                    _movieGenres.clear()
                    _movieGenres.add(defaultGenre)
                    selectedGenre.value = defaultGenre
                    results.data?.genres?.forEach {
                        _movieGenres.add(it)
                    }
                }
                is Resource.Error -> {
                    _movieState.update {
                        it.copy(isLoading = false)
                    }
                }
                else ->{}
            }
        }
    }

    // find where each of these functions are called and see if an if else function can work in those places
    private fun getPopularMovies(
        forceFetchFromRemote: Boolean,
        isRefresh: Boolean = false,
        genreId: Int?,
    ) {
        viewModelScope.launch {
            _movieState.update {
                it.copy(isLoading = true)
            }
            popularRepository.getPopular(
                forceFetchFromRemote,
                "popular",
                isRefresh,
                movieListState.value.popularMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { popularList ->
                            val filteredList = genreId?.let { id ->
                                popularList.filter { movie ->
                                    movie.genre_ids.contains(id)
                                }
                            } ?: popularList
                            _movieState.update {
                                it.copy(
                                    popularMoviesList = it.popularMoviesList
                                            + filteredList.shuffled(),
                                    popularMovieListPage = it.popularMovieListPage + 1
                                )
                            }

                        }
                    }

                    is Resource.Loading -> {
                        _movieState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Error -> {
                        _movieState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }

            }
        }

    }

    private fun getUpcomingMovies(
        forceFetchFromRemote: Boolean,
        isRefresh: Boolean = false,
        genreId: Int?,
    ) {
        viewModelScope.launch {
            _movieState.update {
                it.copy(isLoading = true)
            }

            upcomingRepo.getUpcoming(
                forceFetchFromRemote,
                category = "upcoming",
                isRefresh,
                movieListState.value.upComingMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { upcomingList ->
                            val filteredList = genreId?.let { id ->
                                upcomingList.filter { movie ->
                                    movie.genre_ids.contains(id)
                                }
                            } ?: upcomingList
                            _movieState.update {
                                it.copy(
                                    upcomingMovieList = it.upcomingMovieList
                                            + filteredList.shuffled(),
                                    upComingMovieListPage = it.upComingMovieListPage + 1
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _movieState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Error -> {
                        _movieState.update {
                            it.copy(isLoading = true)
                        }
                    }
                }

            }
        }
    }

    private fun getTopRatedMovie(
        forceFetchFromRemote: Boolean,
        isRefresh: Boolean = false,
        genreId: Int?
    ) {
        viewModelScope.launch {
            _movieState.update {
                it.copy(isLoading = true)
            }

            topRatedRepo.getTopRated(
                forceFetchFromRemote,
                category = "top_rated",
                isRefresh,
                movieListState.value.topRatedMovieListPage,
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { topRatedMovies ->
                            val filteredList = genreId?.let { id ->
                                topRatedMovies.filter { movie ->
                                    movie.genre_ids.contains(id)
                                }
                            } ?: topRatedMovies
                            _movieState.update {
                                it.copy(
                                    topRatedMovieListPage = it.topRatedMovieListPage + 1,
                                    topRatedTvMovieList = it.topRatedTvMovieList
                                            + filteredList.shuffled()
                                )
                            }
                            // Log.d(TAG, "TopRatedSeries = ${getTopRatedTvSeries(forceFetchFromRemote, isRefresh)}")
                        }
                    }

                    is Resource.Loading -> {
                        _movieState.update {
                            it.copy(result.isLoading)
                        }
                    }

                    is Resource.Error -> {
                        _movieState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }

            }
        }

    }

    private fun getTrendingAll(
        fetchFromRemote: Boolean,
        isRefresh: Boolean = false,
        genreId: Int?,
    ) {
        viewModelScope.launch {
            _movieState.update {
                it.copy(isLoading = true)
            }
            movieRepository.getTrending(
                fetchFromRemote,
                "day",
                isRefresh,
                movieListState.value.trendingAllPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { trendingMovies ->
                            val filteredList = genreId?.let { id ->
                                trendingMovies.filter { movie ->
                                    movie.genre_ids.contains(id)
                                }
                            } ?: trendingMovies
                            filteredList.toMutableList().shuffle()
                                _movieState.update {
                                    it.copy(
                                        trendingAllList = it.trendingAllList + filteredList.shuffled(),
                                        trendingAllPage = movieListState.value.trendingAllPage + 1
                                    )
                                }

                            Log.d(TAG, "TrendingMovies = ${filteredList.size}")
                            Log.d("ViewModel", "Updated trending movies list with ${filteredList.size} items")
                        }
                    }

                    is Resource.Loading -> {
                        _movieState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Error -> {
                        _movieState.update {
                            it.copy(isLoading = false)
                        }
                    }

                }
            }
        }
    }

    private fun getNowPlayingMovies(
        fetchFromRemote: Boolean,
        isRefresh: Boolean = false,
        genreId: Int?
    ) {
        viewModelScope.launch {
            nowPlayingRepo.getNowPlaying(
                fetchFromRemote,
                "now_playing",
                isRefresh,
                _movieState.value.nowPlayingMoviesPage
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { nowPlaying ->
                            val filteredList = genreId?.let { id ->
                                nowPlaying.filter { movie ->
                                    movie.genre_ids.contains(id)
                                }
                            } ?: nowPlaying // Ensure filteredList is a list of movies

                            _movieState.update {
                                it.copy(
                                    nowPlayingMoviesList = it.nowPlayingMoviesList
                                            + filteredList.shuffled(),
                                    nowPlayingMoviesPage = it.nowPlayingMoviesPage + 1
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        _movieState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _movieState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }

    fun getRecommendedMovies(
        fetchFromRemote: Boolean,
        isRefresh: Boolean = false,
        movieId:Int,
    ) {
        viewModelScope.launch {
            recomRepository.getRecommended(
                fetchFromRemote,
                movieId,
                "recommendations",
                isRefresh,
                movieListState.value.recommendedPage
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { recommendedMovies ->
//                            val filteredList = genreId?.let { id ->
//                               recommendedMovies.filter { movie ->
//                                    movie.genre_ids.contains(id)
//                                }
//                            } ?: recommendedMovies
                            //if (isRefresh) {
                                _movieState.update {
                                    it.copy(
                                        recommendedMoviesList = it.recommendedMoviesList + recommendedMovies.shuffled(),
                                        recommendedPage = it.recommendedPage + 1
                                    )

                                }
                            //}
                        }
                    }

                    is Resource.Loading -> {
                        _movieState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Error -> {
                        _movieState.update {
                            it.copy(isLoading = false)
                        }
                    }

                }
            }
        }
    }

}