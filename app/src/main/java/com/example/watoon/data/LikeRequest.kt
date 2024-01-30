package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LikeRequest(
    @Json(name = "isLike") val isLike: Boolean,
    @Json(name = "isDislike") val isDislike: Boolean,
    @Json(name = "createdBy") val createdBy: Int
)
