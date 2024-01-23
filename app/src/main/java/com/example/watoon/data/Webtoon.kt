package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Webtoon(
    @Json(name = "id") val id:Int,
    @Json(name = "title") val title:String,
    @Json(name = "releasedDate") val nickname:String,
    @Json(name = "author") val author:User,
    @Json(name = "totalRating") val totalRating:String
)
