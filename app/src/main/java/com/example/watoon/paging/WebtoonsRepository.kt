package com.example.watoon.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.watoon.network.MyRestAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val api: MyRestAPI
) {
    fun getWebtoons(type:String) = Pager(
        config = PagingConfig(
            pageSize = 10
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
}