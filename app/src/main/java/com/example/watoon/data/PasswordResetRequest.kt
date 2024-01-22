package com.example.watoon.data

import com.squareup.moshi.Json

data class PasswordResetRequest(
    @Json(name = "email") val email: String,
){}
