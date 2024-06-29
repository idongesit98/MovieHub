package com.zseni.moviehub.data.local.genre

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genreEntities:List<GenreEntity>)

    @Query("SELECT * FROM GenreEntity WHERE category = :mediaType")
    suspend fun getGenresList(mediaType:String):List<GenreEntity>
}