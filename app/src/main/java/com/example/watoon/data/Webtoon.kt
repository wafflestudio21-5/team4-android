package com.example.watoon.data

import com.squareup.moshi.Json

data class Webtoon(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
){
}
