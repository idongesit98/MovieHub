package com.zseni.moviehub.domain.repository

import com.zseni.moviehub.domain.model.Movie
import com.zseni.moviehub.util.Resource
import kotlinx.coroutines.flow.Flow

interface PopularRepository {
    suspend fun getPopular(
        forceFetchFromRemote:Boolean,
        category:String,
        isRefresh:Boolean,
        page:Int,
    ): Flow<Resource<List<Movie>>>

    suspend fun getPopularSeries(
        forceFetchFromRemote:Boolean,
        category:String,
        isRefresh:Boolean,
        page:Int,
    ): Flow<Resource<List<Movie>>>
}