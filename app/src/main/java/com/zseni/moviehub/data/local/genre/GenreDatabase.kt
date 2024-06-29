package com.zseni.moviehub.data.local.genre

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GenreEntity::class], version = 1, exportSchema = false)
abstract class GenreDatabase:RoomDatabase() {
    abstract val genreDao: GenreDao
}