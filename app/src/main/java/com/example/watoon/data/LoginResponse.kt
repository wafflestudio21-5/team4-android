package com.example.watoon.data

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "access") val access: String,
    @Json(name = "user") val user: LoginUser,
){}

data class LoginUser(
    @Json(name = "pk") val id: Int,
){}