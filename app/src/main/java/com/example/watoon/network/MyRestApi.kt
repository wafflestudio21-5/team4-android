package com.example.watoon.network

import com.example.watoon.data.CommentRequest
import com.example.watoon.data.EpisodeListRequest
import com.example.watoon.data.CommentResponse
import com.example.watoon.data.EpisodeContent
import com.example.watoon.data.LoginRequest
import com.example.watoon.data.LoginResponse
import com.example.watoon.data.PasswordResetRequest
import com.example.watoon.data.RecommentResponse
import com.example.watoon.data.RegisterRequest
import com.example.watoon.data.UploadEpisodeRequest
import com.example.watoon.data.UploadWebtoonRequest
import com.example.watoon.data.Webtoon
import com.example.watoon.data.WebtoonDetailRequest
import com.example.watoon.data.WebtoonListRequset
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
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

    @GET("/api/subscribeWebtoonList")
    suspend fun loadMySubscribeWebtoon(
        @Header("Cookie") token: String,
    ) : List<Webtoon>

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
        @Path(value = "list_type") type:String,
        @Query(value = "cursor") cursor:String?
    ):WebtoonListRequset

    @GET("/api/webtoon/{id}/episode")
    suspend fun getEpisodeList(
        @Path(value = "id") id: String,
        @Query(value = "cursor") cursor:String?
    ):EpisodeListRequest

    @GET("/api/webtoon/{id}")
    suspend fun getWebtoonInfo(
        @Header("Cookie") token : String,
        @Path(value = "id") id:String
    ):WebtoonDetailRequest

    @GET("/api/webtoonList/search")
    suspend fun search(
        @Query(value = "search") search : String
    ): List<Webtoon>

    @DELETE("/api/webtoon/{id}")
    suspend fun deleteWebtoon(
        @Header("Cookie") token: String,
        @Path(value = "id") id: String,
    )

    @GET("/api/episode/{id}/comment")
    suspend fun getComment(
        @Header("Cookie") token:String,
        @Path(value = "id") id: String,
        @Query(value = "cursor") cursor: String?
    ) : CommentResponse

    @DELETE("/api/comment/{id}")
    suspend fun deleteComment(
        @Header("Cookie") token: String,
        @Path(value = "id") id: String,
    ) : Response<Unit>

    @POST("/api/episode/{id}/comment")
    suspend fun uploadComment(
        @Header("Cookie") token : String,
        @Path(value = "id") id : String,
        @Body data : CommentRequest
    )

    @GET("/api/comment/{id}/comment")
    suspend fun getRecomment(
        @Header("Cookie") token:String,
        @Path(value = "id") id : String,
        @Query(value = "cursor") cursor : String?
    ) : RecommentResponse

    @POST("/api/comment/{id}/comment")
    suspend fun uploadRecomment(
        @Header("Cookie") token : String,
        @Path(value = "id") id : String,
        @Body data : CommentRequest
    )

    @GET("/api/episode/{id}")
    suspend fun getEpisodeInfo(
        @Path(value = "id") id:String
    ): EpisodeContent

    @POST("api/webtoon/{id}/subscribe")
    suspend fun changeSubscribe(
        @Header("Cookie") token:String,
        @Path(value = "id") id:String
    )
}