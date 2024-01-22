package com.example.watoon.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watoon.NavigationDestination
import com.example.watoon.data.Webtoon
import com.example.watoon.viewModel.UploadViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun WebtoonUploadPage(onEnter: (String) -> Unit) {
    var isLoading by remember { mutableStateOf(false) }

    val viewModel: UploadViewModel = hiltViewModel()

    val myWebtoonList by viewModel.myWebtoonList.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onEnter(NavigationDestination.Main)
                }
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Text(
                text = "업로드할 웹툰 고르기",
                modifier = Modifier.weight(1f)
            )
        }

        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                try{
                    isLoading = true
                    viewModel.loadMyWebtoon()
                } catch(e : HttpException){

                } finally{
                    isLoading = false
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*items(myWebtoonList) {webtoon ->
                WebtoonItem(webtoon = webtoon, onClick = { onEnter(NavigationDestination.EpisodeUpload) })
            }
            if (isLoading) {
                item {
                    Text("로딩 중입니다...")
                }
            }*/
            items(1){
                WebtoonItem(webtoon = Webtoon(123, "웹툰1"), onClick = { onEnter(NavigationDestination.EpisodeUpload)})
            }
            item{
                MenuButton(text = "새 웹툰"){
                    onEnter(NavigationDestination.NewWebtoon)
                }
            }
        }
    }
}

@Composable
fun WebtoonItem(webtoon: Webtoon, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() },){
        Text(webtoon.title)
    }
}

