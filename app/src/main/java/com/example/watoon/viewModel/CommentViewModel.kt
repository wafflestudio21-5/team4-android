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
    private val repository: Repository
) : ViewModel()  {
    private val _commentList: MutableStateFlow<PagingData<CommentContent>> = MutableStateFlow(value = PagingData.empty())
    val commentList: MutableStateFlow<PagingData<CommentContent>> = _commentList

    suspend fun getComment(id : String){
        repository.getComment(id).cachedIn(viewModelScope).collect{
            _commentList.value = it
        }
    }

    suspend fun deleteComment(id : String){
        repository.deleteComment(id)
    }
}