package com.zseni.moviehub.domain.repository

import com.zseni.moviehub.domain.model.Movie
import com.zseni.moviehub.util.Resource
import kotlinx.coroutines.flow.Flow

interface UpcomingRepository {
    suspend fun getUpcoming(
        forceFetchFromRemote:Boolean,
        category:String,
        isRefresh:Boolean,
        page:Int,
    ): Flow<Resource<List<Movie>>>

    suspend fun getUpcomingSeries(
        forceFetchFromRemote:Boolean,
        category:String,
        isRefresh:Boolean,
        page:Int,
    ): Flow<Resource<List<Movie>>>
}