package com.example.watoon.viewModel

import androidx.lifecycle.ViewModel
import com.example.watoon.data.Webtoon
import com.example.watoon.network.MyRestAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class WebtoonViewModel @Inject constructor(private var api : MyRestAPI) : ViewModel(){
    val webtoonList:MutableStateFlow<List<Webtoon>> = MutableStateFlow(emptyList())

    suspend fun getWebtoonList(type:String){
        api.getWebtoonList(type).apply{
            webtoonList.value = this.results
        }
    }

}