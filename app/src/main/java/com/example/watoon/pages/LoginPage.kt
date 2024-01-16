package com.example.watoon.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.watoon.NavigationDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage (
    onEnter: (String) -> Unit,
){
    var userId by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("ID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
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