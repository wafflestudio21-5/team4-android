package com.example.watoon.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.watoon.data.Image
import com.example.watoon.function.getToken
import com.example.watoon.network.MyRestAPI
import java.lang.Exception

class ImagePagingSource(
    val episodeId : String,
    private var api : MyRestAPI
) : PagingSource<String, Image>(){
    override fun getRefreshKey(state: PagingState<String, Image>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Image> {
        return try {
            val cursor = params.key
            val response = api.getImages(getToken(), episodeId,cursor)

            val previousIndex = response.previous.toString().indexOf("cursor=")
            val nextIndex = response.next.toString().indexOf("cursor=")

            val previous = if(response.previous == null) null else response.previous.toString().substring(previousIndex + "cursor=".length)
            val next = if(response.next == null) null else response.next.toString().substring(nextIndex+"cursor=".length)

            LoadResult.Page(
                data = response.results,
                prevKey = previous,
                nextKey = next
            )
        } catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}