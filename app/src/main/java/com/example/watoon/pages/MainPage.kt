package com.example.watoon.pages

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.watoon.viewModel.WebtoonsViewModel
import com.example.watoon.data.User
import com.example.watoon.data.Webtoon
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPage() {
    val viewModel: WebtoonsViewModel = hiltViewModel()
    val webtoonList = viewModel.webtoonList.collectAsLazyPagingItems()

    val scope = rememberCoroutineScope()

    val calendar = Calendar.getInstance()
    var listNum by remember { mutableIntStateOf(calendar.get(Calendar.DAY_OF_WEEK)-1) }

    val apiListNames = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "finished")
    val appListNames = arrayOf("일", "월", "화", "수", "목", "금", "토", "완결")

    val rowNum = 1 + (webtoonList.itemCount-1)/3
    val context = LocalContext.current

    LaunchedEffect(true){
        try {
            viewModel.getWebtoons(apiListNames[listNum])
        } catch (e: HttpException) {
           makeError(context, e)
        }
    }
    
    Scaffold(
        topBar = {
            // TopBar content
            TopAppBar(
                title = {
                    Text(text = " ")
                },
                actions = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        for(i in 0..7){
                            Text(
                                text= appListNames[i],
                                color = if(i==listNum) Color.Green else Color.White,
                                modifier = Modifier
                                    .clickable {
                                        listNum = i
                                        scope.launch {
                                            try {
                                                viewModel.getWebtoons(apiListNames[listNum])
                                            } catch(e : HttpException){
                                                makeError(context, e)
                                            }
                                        }
                                    }

                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Black)
            )
        }
        , containerColor = Color.Black
    ){
        LazyColumn(
            //padding 방법 추가 고려 필요
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp)
        ){
            items(rowNum){ rowIndex ->
                val rangeStart = rowIndex*3
                var rangeEnd = rangeStart + 2
                if(rangeEnd >= webtoonList.itemCount) rangeEnd = webtoonList.itemCount-1
                RowOfWebtoon(webtoonList.itemSnapshotList.slice(rangeStart .. rangeEnd))
            }
        }
    }
}

fun makeError(context: Context, e:HttpException){
    var message = ""
    val errorBody = JSONObject(e.response()?.errorBody()?.string())
    errorBody.keys().forEach { key ->
        message += ("$key - ${errorBody.getString(key).substring(2 until errorBody.getString(key).length - 2)}" + "\n")
    }
    message = message.substring(0, message.length-1)
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

@Composable
fun RowOfWebtoon(rowList: List<Webtoon?>){
    val emptyWebtoon = Webtoon(
        id = 0, title = " ", releasedDate = " ", totalRating = " ",
        author = User(
            nickname = " ", email = " ", password = null
        ), subscribing = false
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        for(i in 0..2){
            if(i<rowList.size){
                rowList[i]?.let { Webtoon(it) }
            }
            else{
                Webtoon(emptyWebtoon)
            }
        }
    }
}

@Composable
fun Webtoon(webtoon: Webtoon) {
    Row {
        Text(
            text = webtoon.title,
            color = Color.White
        )
    }
}

