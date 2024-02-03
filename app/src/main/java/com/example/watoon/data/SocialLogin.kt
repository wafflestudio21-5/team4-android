package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SocialLogin(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "code") val code: String,
    @Json(name = "id_token") val idToken: String,
){}