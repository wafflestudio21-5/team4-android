package com.example.watoon.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watoon.NavigationDestination
import com.example.watoon.function.LoginButton
import com.example.watoon.function.MyTextField
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun LoginPage (
    onEnter: (String) -> Unit,
){
    val viewModel: LoginViewModel = hiltViewModel()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = com.example.watoon.R.drawable.watoon),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )

        MyTextField(
            value = email,
            onValueChange = { email = it },
            label = "이메일 주소",
            visible = true
        )

        MyTextField(
            value = password,
            onValueChange = { password = it },
            label = "비밀번호",
            visible = false
        )

        LoginButton(text = "로그인") {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    viewModel.login(email, password)
                    Toast.makeText(context, "로그인 성공", Toast.LENGTH_LONG).show()
                    onEnter(NavigationDestination.Main)
                } catch(e : HttpException){
                    makeError(context, e)
                } finally {
                    isLoading = false
                }
            }
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(1.dp)){
            Text(text = "회원가입",
                fontSize = 13.sp,
                color = Color.Black,
                modifier = Modifier.clickable { onEnter(NavigationDestination.CreateAccount) }
            )

            Text(text = "  |  ",
                fontSize = 13.sp)

            Text(text = "비밀번호 찾기",
                color = Color.Black,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onEnter(NavigationDestination.EmailSent) }
            )
        }

        if (isLoading) {
            Text(text = "\n로딩 중입니다...",
                fontSize = 13.sp)
        }

        Text("\n\n")
    }
}
