package com.example.watoon.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.watoon.NavigationDestination

@Composable
fun CreateAccountPage (onEnter: (String) -> Unit){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("create account")
        MenuButton(text = "비밀번호 reset"){
            onEnter(NavigationDestination.EmailSent)
        }
    }
}