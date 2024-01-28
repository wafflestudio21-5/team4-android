package com.example.watoon.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.watoon.data.Webtoon
import com.example.watoon.paging.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class WebtoonsViewModel @Inject constructor(
    private val repository: Repository,
): ViewModel() {
    private val _webtoonList: MutableStateFlow<PagingData<Webtoon>> = MutableStateFlow(value = PagingData.empty())
    val webtoonList: MutableStateFlow<PagingData<Webtoon>> = _webtoonList

    suspend fun getWebtoons(type:String){
        repository.getWebtoons(type).cachedIn(viewModelScope).collect{
            _webtoonList.value = it
        }
    }
}