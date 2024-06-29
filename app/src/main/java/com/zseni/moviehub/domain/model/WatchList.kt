package com.zseni.moviehub.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_list_table")
data class WatchList(
    @PrimaryKey
    val mediaId:Int,
    val imagePath:String,
    val title:String,
    val releaseDate:String,
    val rating:Double,
    val addedOn:String
)
