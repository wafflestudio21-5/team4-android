package com.example.watoon.viewModel

import androidx.lifecycle.ViewModel
import com.example.watoon.data.EpisodeContent
import com.example.watoon.data.WebtoonDetailRequest
import com.example.watoon.network.MyRestAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private var api : MyRestAPI
) : ViewModel(){
    val episodeInfo: MutableStateFlow<EpisodeContent> = MutableStateFlow(EpisodeContent())

    suspend fun getEpisodeContent(episodeId:String){
        episodeInfo.value = api.getEpisodeInfo(episodeId)
    }
}