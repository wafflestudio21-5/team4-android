package com.example.watoon.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.watoon.NavigationDestination
import com.example.watoon.data.User
import com.example.watoon.data.Webtoon
import com.example.watoon.viewModel.UploadViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

@Composable
fun WebtoonUploadPage(viewModel: UploadViewModel, onEnter: (String) -> Unit) {
    var isLoading by remember { mutableStateOf(false) }

    val myWebtoonList by viewModel.myWebtoonList.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current

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
                text = "업로드할 웹툰 선택",
                modifier = Modifier.weight(1f)
            )
        }

        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                try{
                    isLoading = true
                    viewModel.loadMyWebtoon()
                } catch(e : HttpException){
                    var message = ""
                    val errorBody = JSONObject(e.response()?.errorBody()?.string())
                    errorBody.keys().forEach { key ->
                        message += ("$key - ${errorBody.getString(key)}" + "\n")
                    }
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                } finally{
                    isLoading = false
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(myWebtoonList) {webtoon ->
                WebtoonItem(true, webtoon = webtoon, onClick = {
                    viewModel.webtoonId.value = webtoon.id
                    onEnter(NavigationDestination.EpisodeUpload) })
            }
            if (isLoading) {
                item {
                    Text("로딩 중입니다...")
                }
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
fun WebtoonItem(delete: Boolean, webtoon: Webtoon, onClick: () -> Unit) {
    val viewModel: UploadViewModel = hiltViewModel()

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }){
        val context = LocalContext.current

        Text(webtoon.title, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
        if(delete) {
            IconButton(
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            viewModel.deleteWebtoon(webtoon.id)
                            viewModel.loadMyWebtoon()
                        } catch (e: HttpException) {
                            var message = ""
                            val errorBody = JSONObject(e.response()?.errorBody()?.string())
                            errorBody.keys().forEach { key ->
                                message += ("$key - ${errorBody.getString(key)}" + "\n")
                            }
                            message = message.substring(0, message.length - 1)
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
            }
        }
    }
}

