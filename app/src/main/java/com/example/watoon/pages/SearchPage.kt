package com.example.watoon.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.watoon.NavigationDestination
import com.example.watoon.R
import com.example.watoon.data.Webtoon
import com.example.watoon.function.ToMainWebtoonItem
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.UploadViewModel
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

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
                contentDescription = "",
                modifier = Modifier.clickable {
                    onEnter(NavigationDestination.Main)
                }
            )
            TextField(
                value = keyword,
                onValueChange = { keyword = it },
                label = { Text("검색어를 입력하세요.", color = Color.LightGray) },
                modifier = Modifier
                    .padding(8.dp)
                    .border(1.dp, Color.LightGray)
                    .weight(1f),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    placeholderColor = Color.LightGray
                )
            )
            Image(
                painter = painterResource(R.drawable.baseline_search_24),
                contentDescription = "",
                modifier = Modifier.clickable{
                    scope.launch {
                        try{
                            isLoading = true
                            viewModel.search(keyword)
                        } catch(e : HttpException){
                            makeError(context, e)
                        } finally{
                            isLoading = false
                        }
                    }
                }
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(searchWebtoonList) {
                ToMainWebtoonItem(webtoon = it, toWebtoonMain = toWebtoonMain)
            }
            if (isLoading) {
                item {
                    Text(text = "로딩 중입니다...",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
