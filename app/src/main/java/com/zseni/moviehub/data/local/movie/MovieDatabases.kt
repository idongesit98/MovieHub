package com.zseni.moviehub.data.local.movie

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.zseni.moviehub.domain.model.WatchList


@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase:RoomDatabase() {
    abstract val movieDao: MovieDao
}

@Database(entities = [WatchList::class], version = 1, exportSchema = false)
abstract class WatchDatabase:RoomDatabase() {
    abstract val watchDao: WatchDao
}
