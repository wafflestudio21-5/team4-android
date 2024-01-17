package com.example.watoon.pages

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.watoon.NavigationDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(onEnter: (String) -> Unit) {
    var keyword by remember { mutableStateOf("") }

    Row(modifier = Modifier.fillMaxWidth()){
        IconButton(
            onClick = {
                onEnter(NavigationDestination.Main)
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
        }
        TextField(
            value = keyword,
            onValueChange = { keyword = it },
            label = { Text("검색어를 입력하세요.") },
            modifier = Modifier
                .padding(8.dp)
        )
        IconButton(
            onClick = {
                //리스트 보여주기
            },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        }
    }
}
