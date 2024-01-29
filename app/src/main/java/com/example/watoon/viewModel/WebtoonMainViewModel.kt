package com.example.watoon.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.watoon.data.Episode
import com.example.watoon.data.Webtoon
import com.example.watoon.data.WebtoonDetailRequest
import com.example.watoon.network.MyRestAPI
import com.example.watoon.paging.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class WebtoonMainViewModel @Inject constructor(
    private val repository: Repository,
    private val api:MyRestAPI
) : ViewModel() {
    private val _episodeList: MutableStateFlow<PagingData<Episode>> = MutableStateFlow(value = PagingData.empty())
    val episodeList: MutableStateFlow<PagingData<Episode>> = _episodeList

    val webtoonInfo: MutableStateFlow<WebtoonDetailRequest> = MutableStateFlow(WebtoonDetailRequest())
    suspend fun getWebtoonInfo(webtoonId: String){
        webtoonInfo.value =  api.getWebtoonInfo(webtoonId)
    }

    suspend fun getEpisodeList(webtoonId:String){
        repository.getEpisode(webtoonId).cachedIn(viewModelScope).collect{
            episodeList.value = it
        }
    }

}