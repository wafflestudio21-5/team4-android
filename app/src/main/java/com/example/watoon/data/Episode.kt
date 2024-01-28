package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Episode(
    @Json(name = "id") val id:Int,
    @Json(name = "title") val title:String,
    @Json(name = "episodeNumber") val episodeNumber:Int,
    @Json(name = "rating") val rating:String,
    @Json(name = "releasedDate") val releasedDate:String,
)
