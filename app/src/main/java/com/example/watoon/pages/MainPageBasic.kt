package com.example.watoon.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watoon.NavigationDestination

import com.example.watoon.ui.theme.WatoonTheme


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPageBasic(onEnter: (String) -> Unit,) {
    var showMainPage by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        topBar = {
            // TopBar content
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Gray),
                title = {
                    Text(text = " ")
                },
                actions = {
                    MenuButton(text = "웹툰 업로드"){
                        onEnter(NavigationDestination.WebtoonUpload)
                    }
                    IconButton(
                        onClick = {
                            onEnter(NavigationDestination.Search)
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar (
                containerColor = Color.Gray,
                actions = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ){
                        IconButton(
                            onClick = {
                                showMainPage = true
                            }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                                Text(
                                    text = "MENU",
                                    style = TextStyle(
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                showMainPage = false
                            }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(imageVector = Icons.Default.Face, contentDescription = null)
                                Text(
                                    text = "MY",
                                    style = TextStyle(
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }
                }
            )
        }
    ){
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, bottom = 60.dp)
        ) {
            if(showMainPage){
                MainPage()
            }
            else{
                UserPage()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainPageBasicPreview() {
    WatoonTheme {
        //MainPageBasic()
    }
}