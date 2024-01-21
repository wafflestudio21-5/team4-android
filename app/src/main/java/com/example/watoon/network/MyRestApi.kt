package com.example.watoon.network

import com.example.watoon.data.CustomRegister
import retrofit2.http.Body
import retrofit2.http.POST

interface MyRestAPI {
    @POST("/accounts/")
    suspend fun createAccount(@Body data : CustomRegister): CustomRegister
}