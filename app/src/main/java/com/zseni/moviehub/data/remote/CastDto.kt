package com.zseni.moviehub.data.remote

import com.google.gson.annotations.SerializedName
import com.zseni.moviehub.domain.model.Cast

data class CastDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("cast")
    val castResult: List<Cast>
)
