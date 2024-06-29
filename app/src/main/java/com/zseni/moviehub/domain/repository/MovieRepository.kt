package com.zseni.moviehub.domain.repository


import com.zseni.moviehub.domain.model.Movie
import com.zseni.moviehub.util.Resource
import kotlinx.coroutines.flow.Flow

//Check the getMovieRepo if type:String that is not included is the cause of the issues for details not working
interface MovieRepository {
    suspend fun getTrending(
        forceFetchFromRemote:Boolean,
        time:String,
        isRefresh:Boolean,
        page:Int,
    ):Flow<Resource<List<Movie>>>

    suspend fun getTrendingSeries(
        forceFetchFromRemote:Boolean,
        time:String,
        isRefresh:Boolean,
        page:Int,
    ):Flow<Resource<List<Movie>>>



    suspend fun getMovie(
        id:Int,
    ):Flow<Resource<Movie>>

    suspend fun getTv(
        id:Int
    ):Flow<Resource<Movie>>


    suspend fun insertMovie(movie: Movie)
    suspend fun updateMovie(movie: Movie)

}