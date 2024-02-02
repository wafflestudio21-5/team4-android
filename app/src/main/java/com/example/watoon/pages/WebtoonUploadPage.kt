package com.example.watoon.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.watoon.NavigationDestination
import com.example.watoon.data.Webtoon
import com.example.watoon.function.BasicTopBar
import com.example.watoon.function.BasicWebtoonItem
import com.example.watoon.function.MiniButton
import com.example.watoon.function.MyDialog
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.UploadViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun WebtoonUploadPage(viewModel:UploadViewModel, onEnter: (String) -> Unit) {

    var isLoading by remember { mutableStateOf(false) }
    val myWebtoonList by viewModel.myWebtoonList.collectAsState()

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try{
                isLoading = true
                viewModel.loadMyWebtoon()
            } catch(e : HttpException){
                makeError(context, e)
            } finally{
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BasicTopBar(
            text = "나의 웹툰 목록",
            destination = NavigationDestination.Main,
            destination2 = NavigationDestination.NewWebtoon,
            onEnter = onEnter
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(myWebtoonList) {webtoon ->
                MyWebtoonItem(
                    viewModel = viewModel,
                    webtoon = webtoon,
                    onClick = {
                        viewModel.webtoonId.value = webtoon.id
                        onEnter(NavigationDestination.EpisodeUpload)
                    }
                )
            }
            if (isLoading) {
                item {
                    Text(
                        text = "로딩 중입니다...",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun MyWebtoonItem(viewModel: UploadViewModel, webtoon: Webtoon, onClick: () -> Unit) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.End
    ) {
        BasicWebtoonItem(webtoon, onClick)
        MiniButton(text = "삭제") {
            showDialog = true
        }
    }

    MyDialog(
        text = {
            Row (
                horizontalArrangement = Arrangement.Center
            ){
                Text("정말 지우시겠습니까?")
            }
               },
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = {
            scope.launch {
                try {
                    viewModel.deleteWebtoon(webtoon.id)
                    viewModel.loadMyWebtoon()
                } catch (e: HttpException) {
                    makeError(context, e)
                }
            }
            showDialog = false
        }
    )
}
