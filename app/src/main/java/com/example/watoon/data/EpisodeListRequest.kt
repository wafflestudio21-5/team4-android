package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeListRequest(
    @Json(name = "next") val next:String?,
    @Json(name = "previous") val previous:String?,
    @Json(name = "results") val results:List<Episode>
)
