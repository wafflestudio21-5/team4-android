package com.example.watoon.pages

import android.annotation.SuppressLint
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watoon.ui.theme.WatoonTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPage(webtoonList:List<Int>) {
    //서버에 연결하기 전에는 임의로 List 배정
    val rowNum = 1 + (webtoonList.size-1)/3

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
                        Text("월", color = Color.White)
                        Text("화", color = Color.White)
                        Text("수", color = Color.White)
                        Text("목", color = Color.White)
                        Text("금", color = Color.White)
                        Text("토", color = Color.White)
                        Text("일", color = Color.White)
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
fun RowOfWebtoon(rowList : List<Int>){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        for(i in rowList){
            Webtoon(index = i)
        }
    }
}

@Composable
fun Webtoon(index : Int){
    Text(text = index.toString(), color = Color.White)
    //여기서 Webtoon 하나씩 표시 하면 될듯
    //썸네일 + 제목 + 작가 + 별점
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    WatoonTheme {
        MainPage(webtoonList = mutableListOf<Int>(1,2,3,4,5,6,7,8,9))
    }
}