package com.example.watoon.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.watoon.data.CommentContent
import com.example.watoon.data.EpisodeContent
import com.example.watoon.data.Like
import com.example.watoon.data.Rating
import com.example.watoon.data.WebtoonDetailRequest
import com.example.watoon.function.getToken
import com.example.watoon.network.MyRestAPI
import com.example.watoon.paging.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private var api : MyRestAPI,
    private val repository: Repository
) : ViewModel(){
    val episodeInfo: MutableStateFlow<EpisodeContent> = MutableStateFlow(EpisodeContent())

    private val _commentList: MutableStateFlow<PagingData<CommentContent>> = MutableStateFlow(value = PagingData.empty())
    val commentList: MutableStateFlow<PagingData<CommentContent>> = _commentList

    private val _recommentList: MutableStateFlow<PagingData<CommentContent>> = MutableStateFlow(value = PagingData.empty())
    val recommentList: MutableStateFlow<PagingData<CommentContent>> = _recommentList

    private var episodeId = ""
    var commentId = 0
    var comment : CommentContent? = null

    suspend fun putEpisodeRate(rate:Int){
        api.putEpisodeRate(getToken(),episodeId, Rating(rate))
        getEpisodeContent(episodeId)
    }
    suspend fun getEpisodeRate():Int{
        return api.getEpisodeRate(getToken(), episodeId).rating?: 0
    }
    suspend fun putEpisodeLike(){
        api.putEpisodeLike(getToken(), episodeInfo.value.id.toString())
        getEpisodeContent(episodeId)
    }

    suspend fun deleteLike(commentId: String){
        api.deleteLike(getToken(),commentId)
    }

    suspend fun putLike(commentId: String, isLike: Boolean){
        api.putLike(getToken(),commentId, Like(isLike, !isLike))
    }

    suspend fun getEpisodeContent(episodeIdFirst:String){
        episodeInfo.value = api.getEpisodeInfo(getToken(), episodeIdFirst)
        episodeId = episodeInfo.value.id.toString()
    }
    suspend fun getComment(){
        repository.getComment(episodeId).cachedIn(viewModelScope).collect{
            _commentList.value = it
        }
    }

    suspend fun deleteComment(commentId : String){
        repository.deleteComment(commentId)
    }

    suspend fun uploadComment(content : String){
        repository.uploadComment(episodeId, content)
    }

    suspend fun getRecomment(){
        repository.getRecomment(commentId.toString()).cachedIn(viewModelScope).collect{
            _recommentList.value = it
        }
    }

    suspend fun uploadRecomment(content : String){
        repository.uploadRecomment(commentId.toString(), content)
    }
}