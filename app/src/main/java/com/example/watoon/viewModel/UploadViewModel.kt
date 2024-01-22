package com.example.watoon.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.watoon.data.RegisterRequest
import com.example.watoon.data.Webtoon
import com.example.watoon.network.MyRestAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private var api : MyRestAPI) : ViewModel() {
    val myWebtoonList: MutableStateFlow<List<Webtoon>> = MutableStateFlow(listOf())

    //토큰 추가 필요
    suspend fun loadMyWebtoon() {
        myWebtoonList.value = api.loadMyWebtoon()
    }
}