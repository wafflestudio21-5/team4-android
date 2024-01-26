package com.example.watoon.data

import com.squareup.moshi.Json

data class UploadEpisodeRequest(
    @Json(name = "title") val title: String,
    @Json(name = "episodeNumber") val episodeNumber: Int,
){}