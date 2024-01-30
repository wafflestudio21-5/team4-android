package com.example.watoon.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentContent(
    @Json(name = "id") val id : Int,
    @Json(name = "content") val content : String,
    @Json(name = "dtCreated") val dtCreated : String,
    @Json(name = "dtUpdated") val dtUpdated : String,
    @Json(name = "createdBy") val createdBy : UserInfo,
    @Json(name = "subComments") val subComments : String,
    @Json(name = "likedBy") val likedBy : String,
    @Json(name = "dislikedBy") val dislikedBy : String,
    @Json(name = "liking") val liking:Boolean,
    @Json(name = "disliking") val disliking:Boolean
)

data class UserInfo(
    @Json(name = "id") val id : Int,
    @Json(name = "nickname") val nickname : String
)