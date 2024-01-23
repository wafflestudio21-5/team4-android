package com.example.watoon.pages

import android.annotation.SuppressLint
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPage() {
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = " ")
                },
                actions = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        Text("관심 웹툰", color = Color.White)
                        Text("댓글", color = Color.White)
                    }
                }
            )
        }
        ,containerColor = Color.Black
    ){
        LazyColumn(
            //padding 방법 추가 고려 필요
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp)
        ){

        }
    }
}