package com.zseni.moviehub.data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MovieEntity")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val adult: Boolean,
    val backdrop_path: String,
    val first_air_date: String,
    val name: String,
    val origin_country: String,
    val genre_ids: String,
    val original_language: String,
    val original_title: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val videos:String,
    val vote_average: Double,
    val vote_count: Int,
    val category:String,
    val status:String,
    var mediaType:String,
    var runtime:Int,
    var tagline: String,
    var similarMediaList: String,
)
