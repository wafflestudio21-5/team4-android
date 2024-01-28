package com.example.watoon.data

import com.squareup.moshi.Json

data class DayOfWeek(
    @Json(name = "name") val name:String
)