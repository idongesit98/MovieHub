package com.zseni.moviehub.presentation.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zseni.moviehub.data.movie_series_repository.WatchMovieRepo
import com.zseni.moviehub.domain.model.WatchList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchViewModel @Inject constructor(
   val watchRepo:WatchMovieRepo
):ViewModel() {
    private val _addedToWatchList = mutableIntStateOf(0)
    val addedToWatchList:State<Int> = _addedToWatchList

    private val _watchList = mutableStateOf<Flow<List<WatchList>>>(emptyFlow())

    val watchList: MutableState<Flow<List<WatchList>>> = _watchList

    fun addedToWatchList(movie: WatchList){
        viewModelScope.launch {
            watchRepo.addToWatchList(movie)
        }.invokeOnCompletion {
            movieExists(movie.mediaId)
        }
    }

    fun movieExists(mediaId:Int){
        viewModelScope.launch {
            _addedToWatchList.intValue = watchRepo.movieExists(mediaId)
        }
    }

    fun removeFromList(mediaId: Int){
        viewModelScope.launch {
            watchRepo.removeFromWatchMovie(mediaId)
        }.invokeOnCompletion {
            movieExists(mediaId)
        }
    }

    fun getWatchMoviesList(){
        _watchList.value = watchRepo.getMovieList()
    }

    fun deleteWatchMovie(){
        viewModelScope.launch {
            watchRepo.deleteWatchList()
        }
    }


}