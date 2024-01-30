package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WebtoonDetailRequest(
    @Json(name = "id") val id:Int = 0,
    @Json(name = "title") val title:String = "",
    @Json(name = "description") val description:String = "",
    @Json(name = "uploadDays") val uploadDays:List<DayOfWeek> = emptyList(),
    @Json(name = "author") val author:User = User(),
    @Json(name = "totalRating") val totalRating:String = "",
    @Json(name = "episodeCount") val episodeCount:String = "",
    @Json(name = "isFinished") val isFinished:Boolean = false,
    @Json(name = "tags") val tags:List<Tag> = emptyList(),
    @Json(name = "subscribing") var subscribing:Boolean = false,
    @Json(name = "subscribeCount") val subscribeCount:String = "",
)