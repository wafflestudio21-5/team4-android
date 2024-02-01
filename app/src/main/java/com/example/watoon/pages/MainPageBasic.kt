package com.example.watoon.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.watoon.data.Webtoon
import com.example.watoon.function.MainBasicBottomBar
import com.example.watoon.function.MainBasicTopBar


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPageBasic(
    onEnter: (String) -> Unit,
    toWebtoonMain : (Webtoon) -> Unit
) {
    var showMainPage by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        topBar = { MainBasicTopBar(onEnter = onEnter) },
        bottomBar = {
            MainBasicBottomBar(
            showMainPage = showMainPage,
            onMainPageClick = { showMainPage = true },
            onInterestClick = { showMainPage = false }
            )
        }
    ){
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, bottom = 50.dp)
        ) {
            if(showMainPage){
                MainPage(toWebtoonMain)
            }
            else{
                UserPage(toWebtoonMain)
            }
        }
    }
}

