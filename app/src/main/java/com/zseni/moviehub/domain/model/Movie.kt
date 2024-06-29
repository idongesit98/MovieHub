package com.zseni.moviehub.domain.model


import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val first_air_date: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_title: String,
    val original_name: String,
    val overview: String,
    val mediaType: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val category:String,
    val name:String,
    val runtime:Int?,
    val status:String?,
    val tagline:String?,
    val videos:List<String>?,
    val similarMediaList:List<Int>
)