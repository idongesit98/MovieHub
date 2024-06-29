package com.zseni.moviehub.data.local.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zseni.moviehub.domain.model.WatchList
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchList(movie: WatchList)

    @Query("Delete FROM watch_list_table WHERE mediaId = :mediaId")
    suspend fun removeFromWatchList(mediaId:Int)

    @Query("DELETE FROM watch_list_table")
    suspend fun deleteWatchList()

    @Query("SELECT EXISTS (SELECT 1 FROM watch_list_table WHERE mediaId =:mediaId)")
    suspend fun movieExists(mediaId: Int):Int

    @Query("SELECT * FROM watch_list_table ORDER BY addedOn DESC")
    fun getWatchList(): Flow<List<WatchList>>
}