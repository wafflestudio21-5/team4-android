package com.example.watoon.network

import com.example.watoon.data.LoginRequest
import com.example.watoon.data.PasswordResetRequest
import com.example.watoon.data.RegisterRequest
import com.example.watoon.data.UploadWebtoonRequest
import com.example.watoon.data.Webtoon
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyRestAPI {
    @POST("/accounts/")
    suspend fun createAccount(@Body data : RegisterRequest)

    @POST("/accounts/login/")
    suspend fun login(@Body data : LoginRequest)

    @POST("/accounts/password/reset/")
    suspend fun passwordReset(@Body data : PasswordResetRequest)

    //헤더 추가 필요
    @GET("/api/profile/{id}/uploadWebtoonList")
    suspend fun loadMyWebtoon() : List<Webtoon>

    //헤더 추가 필요
    @POST("/api/webtoonList")
    suspend fun uploadWebtoon(@Body data : UploadWebtoonRequest)
}