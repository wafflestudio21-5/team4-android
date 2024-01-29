package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Webtoon(
    @Json(name = "id") val id:Int = 0,
    @Json(name = "title") val title:String = "",
    @Json(name = "releasedDate") val releasedDate:String = "",
    @Json(name = "author") val author:User = User(),
    @Json(name = "totalRating") val totalRating:String = "",
    @Json(name = "subscribing") val subscribing:Boolean = false,
    @Json(name = "titleImage") val titleImage:String? = ""
)