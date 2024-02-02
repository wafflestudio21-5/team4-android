package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeContent(
    @Json(name = "id") val id:Int = 1,
    @Json(name = "title") val title:String = "",
    @Json(name = "episodeNumber") val episodeNumber:Int = 1,
    @Json(name = "totalRating") val totalRating:String = "",
    @Json(name = "releasedDate") val releasedDate:String = "",
    @Json(name = "webtoon") val webtoon: Webtoon = Webtoon(),
    @Json(name = "previousEpisode") val previousEpisode: String? = "",
    @Json(name = "nextEpisode") val nextEpisode:String? = "",
    @Json(name = "liking") val liking:Boolean = false,
    @Json(name = "likedBy") val likedBy:Int = 0,
    @Json(name = "imageUrls") val imageUrls:String = "",
    @Json(name = "imageDomain") val imageDomain:String = "",
    @Json(name = "imageNumber") val imageNumber:String = "",
    @Json(name = "images") val images:List<Image> = listOf(),
)

@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "image") val image:String = "",
)