package com.zseni.moviehub.data.local.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieItem: MovieEntity)

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id:Int): MovieEntity

    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    suspend fun getMoviesByCategory(category:String):List<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    suspend fun getTvSeriesByCategory(category: String):List<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getTvById(id:Int): MovieEntity

    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    suspend fun searchMovies(category: String):List<MovieEntity>

    @Query("DELETE FROM MovieEntity WHERE category = :category")
    suspend fun deleteMovies(category: String)

    @Query("SELECT * FROM MovieEntity WHERE category = :category")
    suspend fun getTrendingMovies(category:String):List<MovieEntity>

}