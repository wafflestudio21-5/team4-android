package com.example.watoon.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watoon.data.Webtoon
import com.example.watoon.function.ToMainWebtoonItem
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.WebtoonsViewModel
import retrofit2.HttpException

@Composable
fun UserPage(toWebtoonMain : (Webtoon) -> Unit) {
    val viewModel:WebtoonsViewModel = hiltViewModel()

    val subScribeWebtoons by viewModel.subscribeWebtoonList.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(true) {
        try {
            viewModel.getSubscribeWebtoons()
        } catch (e: HttpException) {
            makeError(context, e)
        }
    }
    LazyColumn(
        modifier = Modifier
            .padding(vertical = 15.dp)
    ){
        items(subScribeWebtoons){
            ToMainWebtoonItem(it, toWebtoonMain)
        }
    }
}