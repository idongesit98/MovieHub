package com.zseni.moviehub.data.mapper

import com.zseni.moviehub.data.local.movie.MovieEntity
import com.zseni.moviehub.data.remote.MovieDto
import com.zseni.moviehub.domain.model.Movie


fun MovieDto.toMovieEntity(
    category: String
): MovieEntity {
    return MovieEntity(
       adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        id = id ?:0,
        original_title = original_title ?: "",
        category = category,
        video = video ?: false,
        genre_ids = try {
            genre_ids?.joinToString(",") ?: "-1,-2"
        }catch (e:Exception){
            "-1,-2"
        },
        first_air_date = first_air_date?:"",
        name = name?:"",
        origin_country = try {
            origin_country?.joinToString ( "," )?: "-1,-2"
        }catch (e:Exception){ "-1,-2"},
        original_name = original_name?:"",
        mediaType = media_type?:"",
        similarMediaList = try {
            similarMediaList?.joinToString(",")?:"-1,-2"
        }catch (e:Exception){
            "-1,-2"
        },
        runtime = 0,
        tagline = "",
        status = "",
        videos = ""
    )
}
fun MovieDto.toMovie(
    category: String
): Movie {
    return Movie(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        id = id ?:0,
        original_title = original_title ?: "",
        category = category,
        video = video ?: false,
        genre_ids = genre_ids ?: emptyList(),
        first_air_date = first_air_date?:"",
        name = name?:"",
        origin_country = origin_country?: emptyList(),
        original_name = original_name?:"",
        mediaType = media_type?:"",
        runtime =null,
        status = null,
        videos = videos,
        similarMediaList = similarMediaList?: emptyList(),
        tagline = null
    )
}

fun MovieEntity.toMovie(
    category:String
): Movie {
    return Movie(
        id = id,
        adult = adult,
        backdrop_path = backdrop_path,
        original_language = original_language,
        original_title = original_title,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        video = video,
        vote_average = vote_average,
        vote_count = vote_count,
        category = category,
        genre_ids = try {
            genre_ids.split(",").map { it.toInt() }
        }catch (e:Exception){
            listOf(-1,-2)
        },
        name = name,
        first_air_date = first_air_date,
        original_name = original_name,
        origin_country = try {
            origin_country.split(",").map { it }
        }catch (e:Exception){
            listOf("-1", "-2")
        },
        mediaType = mediaType,
        runtime = runtime?:0,
        similarMediaList = try {
            similarMediaList?.split(",")!!.map { it.toInt() }
        }catch (e:Exception){
            listOf(-1,-2)
        },
        status = status,
        tagline = tagline?:"",
        videos = try {
            videos.split(",")!!.map { it }
        }catch (e:Exception){
            listOf("-1,-2")
        }
    )
}

fun Movie.toMediaEntity(): MovieEntity {
    return MovieEntity(
        adult = adult,
        backdrop_path = backdrop_path,
        original_language = original_language,
        overview = overview,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        vote_average = vote_average,
        popularity = popularity,
        vote_count = vote_count,
        id = id,
        original_title = original_title,
        category = category,
        video = video,
        genre_ids = try {
            genre_ids.joinToString(",")
        }catch (e:Exception){
            "-1,-2"
        },
        first_air_date = first_air_date,
        origin_country = try {
            origin_country.joinToString ( "," )
        }catch (e:Exception){ "-1,-2"},
        original_name = original_name,
        name = name,
        mediaType = mediaType,
        runtime = runtime?:0,
        tagline = tagline?:"",
        status = status?:"",
        videos = try {
            videos?.joinToString(",")?:"-1,-2"
        }catch (e:Exception){
            "-1,-2"
        },
        similarMediaList = try {
            similarMediaList.joinToString (",")
        }catch (e:Exception){
            "-1,-2"
        }
    )
}

