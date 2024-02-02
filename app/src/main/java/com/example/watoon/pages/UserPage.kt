package com.example.watoon.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watoon.data.Webtoon
import com.example.watoon.function.MainWebtoonItem
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.WebtoonsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
            MainWebtoonItem(it, toWebtoonMain)
        }
    }
}