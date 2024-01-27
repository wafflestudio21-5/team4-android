package com.example.watoon.network

import com.example.watoon.data.LoginRequest
import com.example.watoon.data.LoginResponse
import com.example.watoon.data.PasswordResetRequest
import com.example.watoon.data.RegisterRequest
import com.example.watoon.data.UploadEpisodeRequest
import com.example.watoon.data.UploadWebtoonRequest
import com.example.watoon.data.Webtoon
import com.example.watoon.data.WebtoonListRequset
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

import retrofit2.http.Query



interface MyRestAPI {
    @POST("/accounts/")
    suspend fun createAccount(@Body data: RegisterRequest)

    @POST("/accounts/login/")
    suspend fun login(@Body data: LoginRequest): LoginResponse

    @POST("/accounts/password/reset/")
    suspend fun passwordReset(@Body data: PasswordResetRequest)

    @GET("/api/profile/{id}/uploadWebtoonList")
    suspend fun loadMyWebtoon(
        @Path(value = "id") id: String
    ): List<Webtoon>

    @POST("/api/webtoonList")
    suspend fun uploadWebtoon(
        @Header("Cookie") token: String,
        @Body data: UploadWebtoonRequest
    )

    @POST("/api/webtoon/{id}/episode")
    suspend fun uploadEpisode(
        @Header("Cookie") token: String,
        @Path(value = "id") id: String,
        @Body data: UploadEpisodeRequest
    )


    @GET("/api/webtoonList/{list_type}")
    suspend fun getWebtoonList(
        @Path(value = "list_type") type: String,
        @Query(value = "cursor") cursor: String?
    ): WebtoonListRequset

    @GET("/api/webtoonList/search")
    suspend fun search(
        @Query(value = "search") search : String
    ): List<Webtoon>
}