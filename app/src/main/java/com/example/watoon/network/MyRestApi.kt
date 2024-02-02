package com.example.watoon.network

import android.provider.Telephony
import com.example.watoon.data.CommentRequest
import com.example.watoon.data.EpisodeListRequest
import com.example.watoon.data.CommentResponse
import com.example.watoon.data.EpisodeContent
import com.example.watoon.data.Like
import com.example.watoon.data.LikeRequest
import com.example.watoon.data.LoginRequest
import com.example.watoon.data.LoginResponse
import com.example.watoon.data.PasswordResetRequest
import com.example.watoon.data.Rating
import com.example.watoon.data.RatingRequest
import com.example.watoon.data.RecommentResponse
import com.example.watoon.data.RegisterRequest
import com.example.watoon.data.UploadEpisodeRequest
import com.example.watoon.data.UploadWebtoonRequest
import com.example.watoon.data.Webtoon
import com.example.watoon.data.WebtoonDetailRequest
import com.example.watoon.data.WebtoonListRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @Multipart
    @POST("/api/webtoonList")
    suspend fun uploadWebtoon(
        @Header("Cookie") token: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("uploadDays") uploadDays: RequestBody,
        @Part("tags") tags: RequestBody,
        @Part image: MultipartBody.Part
        //@Body data: UploadWebtoonRequest
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
    ):WebtoonListRequest

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
        @Header("Cookie") token : String,
        @Path(value = "id") id:String
    ): EpisodeContent

    @POST("api/webtoon/{id}/subscribe")
    suspend fun changeSubscribe(
        @Header("Cookie") token:String,
        @Path(value = "id") id:String
    )

    @DELETE("api/comment/{id}/like")
    suspend fun deleteLike(
        @Header("Cookie") token : String,
        @Path(value = "id") id:String
    )

    @PUT("api/comment/{id}/like")
    suspend fun putLike(
        @Header("Cookie") token : String,
        @Path(value = "id") id:String,
        @Body data : Like
    ):LikeRequest

    @POST("api/episode/{id}/like")
    suspend fun putEpisodeLike(
        @Header("Cookie") token : String,
        @Path(value = "id") id:String
    )

    @GET("api/episode/{id}/rating")
    suspend fun getEpisodeRate(
        @Header("Cookie") token : String,
        @Path(value = "id") id:String
    ):RatingRequest

    @PUT("api/episode/{id}/rating")
    suspend fun putEpisodeRate(
        @Header("Cookie") token : String,
        @Path(value = "id") id:String,
        @Body data : Rating
    ):RatingRequest
}