package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "nickname") val nickname:String,
    @Json(name = "email") val email:String,
    @Json(name = "password") val password:String?
)
