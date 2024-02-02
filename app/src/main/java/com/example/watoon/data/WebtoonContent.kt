package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WebtoonContent(
    @Json(name = "title") val title: String,
    @Json(name = "description") val descritpion: String,
    @Json(name = "author") val author: User,
    @Json(name = "isFinished") val isFinished: Boolean
){}