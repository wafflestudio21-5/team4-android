package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NickName(
    @Json(name = "nickname") val nickname:String = "",
)
