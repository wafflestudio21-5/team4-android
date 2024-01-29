package com.example.watoon.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.watoon.NavigationDestination
import com.example.watoon.function.MenuButton

@Composable
fun SignupCompletePage (onEnter: (String) -> Unit){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("이메일을 성공적으로 전송하였습니다.")
        Text("이메일을 확인해 주세요.")
        MenuButton(text = "로그인 화면으로"){
            onEnter(NavigationDestination.Login)
        }
    }
}