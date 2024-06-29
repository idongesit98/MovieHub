package com.zseni.moviehub.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zseni.moviehub.domain.repository.MovieRepository
import com.zseni.moviehub.domain.repository.SearchRepository
import com.zseni.moviehub.presentation.screens.searchScreen.components.SearchState
import com.zseni.moviehub.presentation.screens.searchScreen.components.SearchUiEvents
import com.zseni.moviehub.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val searchRepository: SearchRepository
):ViewModel() {
    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    private var searchJob:Job? = null

    fun onEvent(event: SearchUiEvents){
        when(event){
            is SearchUiEvents.OnSearchItemClicked ->{
                viewModelScope.launch {
                    movieRepository.updateMovie(event.movie)
                }
            }

            is SearchUiEvents.OnSearchQueryChanged ->{
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)

                    _searchState.update {
                        it.copy(
                            searchQuery = event.query,
                            searchList = emptyList()
                        )
                    }
                    loadSearchList()
                }
            }

            is SearchUiEvents.OnPaginate->{
                _searchState.update {
                    it.copy(
                        searchPage = searchState.value.searchPage +1
                    )
                }
            }

            is SearchUiEvents.Refresh ->{}
        }
    }

    private fun loadSearchList() {
        viewModelScope.launch {
            searchRepository.getSearchList(
                forceFetchedFromRemote = true,
                query = searchState.value.searchQuery,
                page = searchState.value.searchPage
            ).collect{ result ->
                when(result){
                is Resource.Success -> {
                    result.data?.let { movieList ->
                        _searchState.update {
                            it.copy(searchList = searchState.value.searchList + movieList
                            )
                        }
                    }
                }
                    is Resource.Loading ->{
                        _searchState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Error -> {
                        _searchState.update {
                            it.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }
}