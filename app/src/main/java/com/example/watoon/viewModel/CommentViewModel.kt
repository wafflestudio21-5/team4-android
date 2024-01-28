package com.example.watoon.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.watoon.data.CommentContent
import com.example.watoon.paging.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel()  {
    private val _commentList: MutableStateFlow<PagingData<CommentContent>> = MutableStateFlow(value = PagingData.empty())
    val commentList: MutableStateFlow<PagingData<CommentContent>> = _commentList

    private val _recommentList: MutableStateFlow<PagingData<CommentContent>> = MutableStateFlow(value = PagingData.empty())
    val recommentList: MutableStateFlow<PagingData<CommentContent>> = _recommentList

    var episodeId = 0
    var commentId = 0
    var comment : CommentContent? = null

    suspend fun getComment(episodeId : String){
        repository.getComment(episodeId).cachedIn(viewModelScope).collect{
            _commentList.value = it
        }
    }

    suspend fun deleteComment(commentId : String){
        repository.deleteComment(commentId)
    }

    suspend fun uploadComment(episodeId : String, content : String){
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