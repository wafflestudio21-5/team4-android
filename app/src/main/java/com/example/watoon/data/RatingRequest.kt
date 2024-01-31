package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class RatingRequest(
    @Json(name = "rating") val rating:Int?,
    @Json(name = "createdBy") val createdBy :Int?
)