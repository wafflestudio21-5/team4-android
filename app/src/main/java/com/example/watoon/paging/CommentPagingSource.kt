package com.example.watoon.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.watoon.data.CommentContent
import com.example.watoon.function.getToken
import com.example.watoon.network.MyRestAPI

class CommentPagingSource(
    val episodeId : String,
    private var api : MyRestAPI
):PagingSource<String, CommentContent>() {
    override fun getRefreshKey(state: PagingState<String, CommentContent>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, CommentContent> {
        return try {
            val cursor = params.key
            val response = api.getComment(getToken(), episodeId, cursor)

            val previousIndex = response.previous.toString().indexOf("cursor=")
            val nextIndex = response.next.toString().indexOf("cursor=")

            val previous = if (response.previous == null) null else
                java.net.URLDecoder.decode(response.previous.toString().substring(previousIndex + "cursor=".length), "UTF-8")

            val next = if (response.next == null) null else
                java.net.URLDecoder.decode(response.next.toString().substring(nextIndex + "cursor=".length), "UTF-8")

            LoadResult.Page(
                data = response.results,
                prevKey = previous,
                nextKey = next
            )
        } catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}