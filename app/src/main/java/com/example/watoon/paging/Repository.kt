package com.example.watoon.paging

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.watoon.MyApp
import com.example.watoon.data.CommentRequest
import com.example.watoon.function.getToken
import com.example.watoon.network.MyRestAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val api: MyRestAPI
) {
    fun getWebtoons(type:String) = Pager(
        config = PagingConfig(
            pageSize = 20
        ),
        pagingSourceFactory = {
            WebtoonsPagingSource(type = type,api = api)
        }
    ).flow

    fun getEpisode(webtoonId:String) = Pager(
        config = PagingConfig(
            pageSize = 20
        ),
        pagingSourceFactory = {
            EpisodePagingSource(webtoonId = webtoonId, api = api)
        }
    ).flow

    fun getComment(id: String) = Pager(
        config = PagingConfig(
            pageSize = 20
        ),
        pagingSourceFactory = {
            CommentPagingSource(episodeId = id, api = api)
        }
    ).flow

    fun getImages(episodeId: String) = Pager(
        config = PagingConfig(
            pageSize = 20
        ),
        pagingSourceFactory = {
            ImagePagingSource(episodeId = episodeId, api = api)
        }
    ).flow

    suspend fun deleteComment(id: String){
        api.deleteComment(getToken(), id)
    }

    suspend fun uploadComment(episodeId : String, content : String){
        val commentRequest = CommentRequest(content)
        api.uploadComment(getToken(), episodeId, commentRequest)
    }

    fun getRecomment(id: String) = Pager(
        config = PagingConfig(
            pageSize = 20
        ),
        pagingSourceFactory = {
            RecommentPagingSource(episodeId = id, api = api)
        }
    ).flow

    suspend fun uploadRecomment(episodeId : String, content : String){
        val commentRequest = CommentRequest(content)
        api.uploadRecomment(getToken(), episodeId, commentRequest)
    }
}