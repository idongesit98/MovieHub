package com.zseni.moviehub.domain.repository

import com.zseni.moviehub.domain.model.Movie
import com.zseni.moviehub.util.MovieType
import com.zseni.moviehub.util.Resource
import kotlinx.coroutines.flow.Flow

interface SimilarRepos {
    suspend fun getSimilar(
        forceFetchFromRemote:Boolean,
        category:String,
        isRefresh:Boolean,
        page:Int,
        movieType: MovieType
    ): Flow<Resource<List<Movie>>>
}