package com.example.watoon.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.watoon.NavigationDestination
import com.example.watoon.data.Webtoon
import com.example.watoon.viewModel.UploadViewModel
import com.example.watoon.viewModel.WebtoonsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(
    viewModel:UploadViewModel,
    onEnter: (String) -> Unit,
    toWebtoonMain : (Webtoon) -> Unit
) {

    var keyword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val searchWebtoonList by viewModel.searchWebtoonList.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = {
                    onEnter(NavigationDestination.Main)
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            TextField(
                value = keyword,
                onValueChange = { keyword = it },
                label = { Text("검색어를 입력하세요.") },
                modifier = Modifier
                    .padding(8.dp)
            )
            IconButton(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        try{
                            isLoading = true
                            viewModel.search(keyword)
                        } catch(e : HttpException){

                        } finally{
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(searchWebtoonList) {
                SearchWebtoonItem(webtoon = it, toWebtoonMain = toWebtoonMain)
            }
            if (isLoading) {
                item {
                    Text("로딩 중입니다...")
                }
            }
        }
    }
}
@Composable
fun SearchWebtoonItem(
    webtoon: Webtoon,
    toWebtoonMain : (Webtoon) -> Unit
){
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { toWebtoonMain(webtoon) }){
        Text(webtoon.title, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
    }
}