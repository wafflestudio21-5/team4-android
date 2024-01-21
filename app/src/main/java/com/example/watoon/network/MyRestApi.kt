package com.example.watoon.network

import com.example.watoon.data.LoginRequest
import com.example.watoon.data.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface MyRestAPI {
    @POST("/accounts/")
    suspend fun createAccount(@Body data : RegisterRequest)

    @POST("/accounts/login/")
    suspend fun login(@Body data : LoginRequest)
}