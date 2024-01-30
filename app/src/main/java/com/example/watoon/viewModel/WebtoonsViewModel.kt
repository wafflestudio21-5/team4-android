package com.example.watoon.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.watoon.MyApp
import com.example.watoon.data.Webtoon
import com.example.watoon.network.MyRestAPI
import com.example.watoon.paging.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class WebtoonsViewModel @Inject constructor(
    private val repository: Repository,
    private val api:MyRestAPI
): ViewModel() {
    private val _webtoonList: MutableStateFlow<PagingData<Webtoon>> = MutableStateFlow(value = PagingData.empty())
    val webtoonList: MutableStateFlow<PagingData<Webtoon>> = _webtoonList

    val subscribeWebtoonList : MutableStateFlow<List<Webtoon>> = MutableStateFlow(listOf())

    suspend fun getWebtoons(type:String){
        repository.getWebtoons(type).cachedIn(viewModelScope).collect{
            _webtoonList.value = it
        }
    }

    suspend fun getSubscribeWebtoons(){
        subscribeWebtoonList.value = api.loadMySubscribeWebtoon(
            "access=" + MyApp.preferences.getToken("token", "")
        )
    }
}