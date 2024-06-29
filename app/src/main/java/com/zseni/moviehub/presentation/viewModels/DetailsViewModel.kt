package com.zseni.moviehub.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zseni.moviehub.domain.model.Cast
import com.zseni.moviehub.domain.repository.GenreRepository
import com.zseni.moviehub.domain.repository.MovieRepository
import com.zseni.moviehub.presentation.screens.movieDetails.DetailsState
import com.zseni.moviehub.util.MovieType
import com.zseni.moviehub.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val genreRepository: GenreRepository,
    savedStateHandle: SavedStateHandle
):ViewModel() {
    private val DAG = DetailsViewModel::class.simpleName

    private val movieId = savedStateHandle.get<Int>("movieId")
    private val tvId = savedStateHandle.get<Int>("tvId")
    private var _movieCast = mutableStateOf<List<Cast>>(emptyList())
    val movieCast:State<List<Cast>> = _movieCast

    private val _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()
    init {
        getMovie(movieId?: -1)
        getTv(tvId?:-1)
    }

    private fun getMovie(id:Int){
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }

           movieRepository.getMovie(id).collectLatest { result ->
               when(result){
                   is Resource.Error ->{
                       _detailsState.update {
                           it.copy(isLoading = false)
                       }
                   }

                   is Resource.Loading ->{
                       _detailsState.update {
                           it.copy(isLoading = result.isLoading)
                       }
                   }

                   is Resource.Success -> {
                       result.data?.let { movie->
                           _detailsState.update {
                               it.copy(movie = movie )
                           }
                       }
                   }
               }
           }
        }
    }

    private fun getTv(id: Int){
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }
            movieRepository.getTv(id).collectLatest { result ->
                when(result){
                    is Resource.Error ->{
                        _detailsState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success ->{
                        result.data?.let {tv ->
                            _detailsState.update {
                                it.copy(tv = tv)
                            }
                        }
                    }
                    is Resource.Loading->{
                        _detailsState.update {
                            it.copy(isLoading = result.isLoading)
                        }

                    }
                }

            }
        }
    }

    fun getMovieCast(movieId:Int,movieType: MovieType){
        viewModelScope.launch {
            genreRepository.getCast(movieId = movieId, movieType = movieType).also {
                if (it is Resource.Success){
                    _movieCast.value = it.data!!.castResult
                }
                Log.e(DAG,"Cast = ${_movieCast.value.size}")
            }

        }
    }
}