package com.zseni.moviehub.data.movie_series_repository

import com.zseni.moviehub.data.local.movie.WatchDatabase
import com.zseni.moviehub.domain.model.WatchList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WatchMovieRepo @Inject constructor(private val database: WatchDatabase) {

    suspend fun addToWatchList(movie:WatchList){
        database.watchDao.addToWatchList(movie)
    }

    suspend fun movieExists(mediaId:Int): Int{
        return database.watchDao.movieExists(mediaId)
    }

    fun getMovieList(): Flow<List<WatchList>> {
        return database.watchDao.getWatchList()
    }

    suspend fun deleteWatchList(){
        database.watchDao.deleteWatchList()
    }

    suspend fun removeFromWatchMovie(mediaId: Int){
        database.watchDao.removeFromWatchList(mediaId)
    }

}