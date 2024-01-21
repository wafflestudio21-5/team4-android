package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class CustomRegister (
    @Json(name = "email") val email: String,
    @Json(name = "password1") val password1: String,
    @Json(name = "password2") val password2: String,
    @Json(name = "nickname") val nickname: String,
){
}