package com.zseni.moviehub.domain.repository

import com.zseni.moviehub.domain.model.Movie
import com.zseni.moviehub.util.Resource
import kotlinx.coroutines.flow.Flow

interface RecomRepository {
    suspend fun getRecommended(
        forceFetchFromRemote:Boolean,
        movieId:Int,
        category:String,
        isRefresh:Boolean,
        page:Int,
    ): Flow<Resource<List<Movie>>>

    suspend fun getRecommendedSeries(
        forceFetchFromRemote:Boolean,
        movieId:Int,
        category:String,
        isRefresh:Boolean,
        page:Int,
    ): Flow<Resource<List<Movie>>>
}