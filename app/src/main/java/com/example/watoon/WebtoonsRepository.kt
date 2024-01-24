package com.example.watoon

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.watoon.network.MyRestAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebtoonsRepository @Inject constructor(
    private val api: MyRestAPI
) {
    fun getWebtoons(type:String) = Pager(
        config = PagingConfig(
            pageSize = 5
        ),
        pagingSourceFactory = {
            PagingSource(type = type,api = api)
        }
    ).flow
}