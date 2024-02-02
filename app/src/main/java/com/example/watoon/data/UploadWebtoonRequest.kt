package com.example.watoon.data

import com.squareup.moshi.Json

data class UploadWebtoonRequest(
    @Json(name = "title") val title : String,
    @Json(name = "description") val description : String,
    @Json(name = "uploadDays") val uploadDays : List<UploadDays>,
    @Json(name = "tags") val tags : List<Tag>
){
}

data class UploadDays(
    @Json(name = "name") val name : String
){}