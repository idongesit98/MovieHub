package com.zseni.moviehub.data.local.genre

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GenreEntity")
data class GenreEntity(
    @PrimaryKey
    val id:Int,
    val name:String,
    val category:String
)
