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
import com.zseni.moviehub.presentation.screens.tvScreen.TvState
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
class TvSeriesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val upcomingRepo: UpcomingRepository,
    private val nowPlayingRepo: NowPlayingRepo,
    private val topRatedRepo: TopRatedRepo,
    private val popularRepository: PopularRepository,
    private val recomRepository: RecomRepository,
    private val similarRepos: SimilarRepos,
    private val genreRepository: GenreRepository
) : ViewModel() {
    private val TAG = TvSeriesViewModel::class.simpleName
    private var _tvGenres = mutableStateListOf(Genre(null, "All"))
    val tvGenres: SnapshotStateList<Genre> = _tvGenres
    var selectedGenre: MutableState<Genre> = mutableStateOf(Genre(null, "All"))
    private val _selectedTvType = MutableStateFlow(MovieType.TvSeries)
    var selectedTvType: StateFlow<MovieType> = _selectedTvType.asStateFlow()
    private val _tvState = MutableStateFlow(TvState())
    val tvState:StateFlow<TvState> = _tvState.asStateFlow()
    var randomMovieId:Int? = null

    init {
        refreshAll()
    }

    fun refreshAll(
        genreId: Int? = selectedGenre.value.id,
    ){
        if (tvGenres.size == 1){
            getTvGenre(selectedTvType.value)
        }
        if (genreId == null){
            selectedGenre.value = Genre(null,"All")
        }
        getTrendingSeries(false, isRefresh = true,genreId)
        getUpcomingSeries(false, isRefresh = true,genreId)
        getPopularSeries(false, isRefresh = true,genreId)
        getNowPlayingSeries(false, isRefresh = true, genreId)
        getTopRatedSeries(false,isRefresh = true,genreId)
        randomMovieId?.let{id -> getRecommendedSeries(false, isRefresh = true, movieId = id,genreId)}

    }

    fun filterBySelectedGenre(genre: Genre) {
        selectedGenre.value = genre
        refreshAll(genre.id)
    }


    fun getTvGenre(movieType: MovieType = selectedTvType.value) {
        viewModelScope.launch {
            val defaultGenre = Genre(null, "All")
            when (val results = genreRepository.getGenre(movieType)) {
                is Resource.Success -> {
                    _tvGenres.clear()
                    _tvGenres.add(defaultGenre)
                    selectedGenre.value = defaultGenre
                    results.data?.genres?.forEach {
                        _tvGenres.add(it)
                    }
                }

                is Resource.Error -> {
                    _tvState.update {
                        it.copy(isLoading = false)
                    }
                }

                is Resource.Loading -> {
                 _tvState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }

    // find where each of these functions are called and see if an if else function can work in those places
    private fun getPopularSeries(
        forceFetchFromRemote: Boolean,
        isRefresh: Boolean = false,
        genreId: Int?,
    ) {
        viewModelScope.launch {
            _tvState.update {
                it.copy(isLoading = true)
            }
            popularRepository.getPopularSeries(
                forceFetchFromRemote,
                "popular",
                isRefresh,
                _tvState.value.popularTvSeriesPage,
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { popularList ->
                            val filteredList = genreId?.let { id ->
                                popularList.filter { movie ->
                                    movie.genre_ids.contains(id)
                                }
                            } ?: popularList
                            _tvState.update {
                                it.copy(
                                    popularTvSeries = it.popularTvSeries
                                            + filteredList.shuffled(),
                                    popularTvSeriesPage = it.popularTvSeriesPage + 1,
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _tvState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Error -> {
                        _tvState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }

            }
        }

    }

    private fun getUpcomingSeries(
        forceFetchFromRemote: Boolean,
        isRefresh: Boolean = false,
        genreId: Int?,
    ) {
        viewModelScope.launch {
            _tvState.update {
                it.copy(isLoading = true)
            }

            upcomingRepo.getUpcomingSeries(
                forceFetchFromRemote,
                category = "on_the_air",
                isRefresh,
                _tvState.value.upComingTvSeriesPage,
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { upcomingList ->
                            val filteredList = genreId?.let { id ->
                                upcomingList.filter { movie ->
                                    movie.genre_ids.contains(id)
                                }
                            } ?: upcomingList
                            _tvState.update {
                                it.copy(
                                    upcomingTvSeries = it.upcomingTvSeries
                                       + filteredList.shuffled(),
                                    upComingTvSeriesPage = it.upComingTvSeriesPage + 1,
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _tvState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Error -> {
                        _tvState.update {
                            it.copy(isLoading = true)
                        }
                    }
                }

            }
        }
    }

    private fun getTopRatedSeries(
        forceFetchFromRemote: Boolean,
        isRefresh: Boolean = false,
        genreId: Int?
    ) {
        viewModelScope.launch {
           _tvState.update {
                it.copy(isLoading = true)
            }

            topRatedRepo.getTopRatedSeries(
                forceFetchFromRemote,
                category = "top_rated",
                isRefresh,
                _tvState.value.topRatedTvSeriesPage,
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { topRatedMovies ->
                            val filteredList = genreId?.let { id ->
                                topRatedMovies.filter { movie ->
                                    movie.genre_ids.contains(id)
                                }
                            } ?: topRatedMovies
                           _tvState.update {
                                it.copy(
                                   topRatedTvSeriesPage = it.topRatedTvSeriesPage + 1,
                                   topRatedTvSeries = it.topRatedTvSeries
                                            + filteredList.shuffled()
                                )
                            }
                            // Log.d(TAG, "TopRatedSeries = ${getTopRatedTvSeries(forceFetchFromRemote, isRefresh)}")
                        }
                    }

                    is Resource.Loading -> {
                        _tvState.update {
                            it.copy(result.isLoading)
                        }
                    }

                    is Resource.Error -> {
                       _tvState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }

            }
        }

    }

    private fun getTrendingSeries(
        fetchFromRemote: Boolean,
        isRefresh: Boolean = false,
        genreId: Int?,
    ) {
        viewModelScope.launch {
            _tvState.update {
                it.copy(isLoading = true)
            }
            movieRepository.getTrendingSeries(
                fetchFromRemote,
                "day",
                isRefresh,
                _tvState.value.trendingTvSeriesPage,
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
                                _tvState.update {
                                    it.copy(
                                        trendingTvSeries = it.trendingTvSeries + filteredList.toList(),
                                        trendingTvSeriesPage = it.trendingTvSeriesPage + 1
                                    )
                                }

                            Log.d(TAG, "TrendingMovies = ${filteredList.size}")
                            Log.d("ViewModel", "Updated trending movies list with ${filteredList.size} items")
                        }
                    }

                    is Resource.Loading -> {
                       _tvState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Error -> {
                        _tvState.update {
                            it.copy(isLoading = false)
                        }
                    }

                }
            }
        }
    }

    private fun getNowPlayingSeries(
        fetchFromRemote: Boolean,
        isRefresh: Boolean = false,
        genreId: Int?
    ) {
        viewModelScope.launch {
            nowPlayingRepo.getNowPlayingSeries(
                fetchFromRemote,
                "airing_today",
                isRefresh,
                _tvState.value.nowPlayingTvSeriesPage,
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { nowPlaying ->
                            val filteredList = genreId?.let { id ->
                                nowPlaying.filter { movie ->
                                    movie.genre_ids.contains(id)
                                }
                            } ?: nowPlaying // Ensure filteredList is a list of movies

                            _tvState.update {
                                it.copy(
                                    nowPlayingTvSeries = it.nowPlayingTvSeries
                                            + filteredList.shuffled(),
                                    nowPlayingTvSeriesPage = it.nowPlayingTvSeriesPage + 1
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        _tvState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _tvState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }

     fun getRecommendedSeries(
        fetchFromRemote: Boolean,
        isRefresh: Boolean = false,
        movieId:Int,
        genreId: Int? = 0,
    ) {
        viewModelScope.launch {
            recomRepository.getRecommendedSeries(
                fetchFromRemote,
                movieId,
                "recommendations",
                isRefresh,
                _tvState.value.recommendedTvSeriesPage
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { recommendedMovies ->
                            val filteredList = genreId?.let { id ->
                               recommendedMovies.filter { movie ->
                                    movie.genre_ids.contains(id)
                                }
                            } ?: recommendedMovies
                            if (isRefresh) {
                                _tvState.update {
                                    it.copy(
                                        recommendedTvSeries = it.recommendedTvSeries
                                                + filteredList.shuffled(),
                                        recommendedTvSeriesPage = it.recommendedTvSeriesPage + 1
                                    )

                                }
                            }
                        }
                    }

                    is Resource.Loading -> {
                       _tvState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Error -> {
                        _tvState.update {
                            it.copy(isLoading = false)
                        }
                    }

                }
            }
        }
    }

}