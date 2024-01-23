package com.example.watoon.pages

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.watoon.NavigationDestination
import com.example.watoon.data.Webtoon
import com.example.watoon.ui.theme.WatoonTheme
import com.example.watoon.viewModel.WebtoonViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPage() {

    val viewModel: WebtoonViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()

    val webtoonList by viewModel.webtoonList.collectAsState()

    val calendar = Calendar.getInstance()
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    val apiListNames = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "finished")
    val appListNames = arrayOf("일", "월", "화", "수", "목", "금", "토", "완결")

    val dayName = apiListNames[dayOfWeek - 1]

    val rowNum = 1 + (webtoonList.size-1)/3

    val context = LocalContext.current

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
                                color = Color.White,
                                modifier = Modifier
                                    .clickable {
                                        scope.launch {
                                            try {
                                                viewModel.getWebtoonList(apiListNames[i])
                                            } catch(e : HttpException){
                                                var message = ""
                                                val errorBody = JSONObject(e.response()?.errorBody()?.string())
                                                errorBody.keys().forEach { key ->
                                                    message += ("$key - ${errorBody.getString(key).substring(2 until errorBody.getString(key).length - 2)}" + "\n")
                                                }
                                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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
                if(rangeEnd > webtoonList.lastIndex) rangeEnd = webtoonList.lastIndex
                RowOfWebtoon(webtoonList.slice(rangeStart..rangeEnd))
            }
        }
    }
}

@Composable
fun RowOfWebtoon(rowList : List<Webtoon>){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        for(i in 0..2){
            if(i<rowList.size){
                Webtoon(rowList[i])
            }
            else{
                //Empty Webtoon
            }
        }
    }
}

@Composable
fun Webtoon(webtoon: Webtoon) {
    Row {
        Text(text = webtoon.title)
    }
}

