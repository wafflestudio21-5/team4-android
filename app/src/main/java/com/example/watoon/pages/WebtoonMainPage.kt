package com.example.watoon.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watoon.viewModel.WebtoonMainViewModel
import retrofit2.HttpException

@Composable
fun WebtoonMainPage(
    webtoonId : Int,
) {
    val viewModel: WebtoonMainViewModel = hiltViewModel()

    val context = LocalContext.current

    LaunchedEffect(true){
        try {
            viewModel.getEpisodeList(webtoonId.toString())
        } catch (e: HttpException) {
            makeError(context, e)
        }
    }

    Column {
        Text(webtoonId.toString())
    }
}