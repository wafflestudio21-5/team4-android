package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeContent(
    @Json(name = "id") val id:Int = 1,
    @Json(name = "title") val title:String = "",
    @Json(name = "episodeNumber") val episodeNumber:Int = 1,
    @Json(name = "rating") val rating:String = "",
    @Json(name = "releasedDate") val releasedDate:String = "",
    @Json(name = "webtoon") val webtoon: Webtoon = Webtoon(),
    @Json(name = "previousEpisode") val previousEpisode: String? = "",
    @Json(name = "nextEpisode") val nextEpisode:String? = "",
    @Json(name = "imageUrl") val imageUrl:String = ""
)
