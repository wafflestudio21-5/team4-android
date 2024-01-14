package com.example.watoon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginPage (
    onEnter: (String) -> Unit,
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("아이디 비번 치는 칸~~")
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(1.dp)){
            MenuButton(text = "로그인") {
                //check login, go to main menu
                onEnter(NavigationDestination.Main)
            }
            MenuButton(text = "회원가입"){
                onEnter(NavigationDestination.CreateAccount)
            }
        }
        MenuButton(text = "비밀번호 reset"){
            onEnter(NavigationDestination.EmailSent)
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(2.dp)
    ) {
        Text(text = text)
    }
}