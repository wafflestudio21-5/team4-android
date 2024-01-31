package com.example.watoon.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watoon.NavigationDestination
import com.example.watoon.R
import com.example.watoon.data.Webtoon
import com.example.watoon.function.BasicTopBar
import com.example.watoon.function.MenuButton
import com.example.watoon.function.UploadTopBar
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.UploadViewModel
import com.example.watoon.viewModel.WebtoonsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
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
        UploadTopBar(
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
                WebtoonItem(
                    viewModel = viewModel,
                    delete = true,
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
fun WebtoonItem(viewModel: UploadViewModel, delete: Boolean, webtoon: Webtoon, onClick: () -> Unit) {

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        //Webtoon 썸네일 필요

        Column(
        ) {
            Text(
                text = webtoon.title,
                modifier = Modifier.padding(10.dp),
                fontSize = 23.sp
            )
            Text(
                text = "등록 날짜 : " + webtoon.releasedDate,
                modifier = Modifier.padding(10.dp),
                fontSize = 15.sp
            )
        }

        //없애도 되는 기능?
        if(delete) {
            IconButton(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            viewModel.deleteWebtoon(webtoon.id)
                            viewModel.loadMyWebtoon()
                        } catch (e: HttpException) {
                           makeError(context, e)
                        }
                    }
                },
            ) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
            }
        }
    }
}

