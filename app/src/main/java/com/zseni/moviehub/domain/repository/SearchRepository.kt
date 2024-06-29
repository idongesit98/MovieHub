package com.zseni.moviehub.domain.repository

import com.zseni.moviehub.domain.model.Movie
import com.zseni.moviehub.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getSearchList(
        forceFetchedFromRemote:Boolean,
        query:String,
        page:Int,
    ):Flow<Resource<List<Movie>>>
}