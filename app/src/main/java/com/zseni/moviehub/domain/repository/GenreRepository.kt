package com.zseni.moviehub.domain.repository

import com.zseni.moviehub.data.remote.CastDto
import com.zseni.moviehub.data.remote.GenreListDto
import com.zseni.moviehub.util.MovieType
import com.zseni.moviehub.util.Resource

interface GenreRepository {
    suspend fun getGenre(
        movieType: MovieType
    ):Resource<GenreListDto>

    suspend fun getCast(
        movieType: MovieType,
        movieId:Int
    ):Resource<CastDto>

}