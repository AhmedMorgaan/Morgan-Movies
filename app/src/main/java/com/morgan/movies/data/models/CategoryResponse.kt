package com.morgan.movies.data.models


import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("genres")
    val genres: MutableList<Genre>
)

data class Genre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
