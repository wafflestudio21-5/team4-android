package com.example.watoon.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watoon.NavigationDestination
import com.example.watoon.function.BasicTopBar
import com.example.watoon.function.MyButton
import com.example.watoon.function.MyText
import com.example.watoon.function.MyTextField
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun EmailSentPage (onEnter: (String) -> Unit){
    val viewModel: LoginViewModel = hiltViewModel()

    var email by rememberSaveable { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicTopBar(
            text = "비밀번호 찾기",
            destination = NavigationDestination.Login,
            onEnter = onEnter
        )

        MyText(text = "\n이메일 주소")
        MyTextField(
            value = email,
            onValueChange = { email = it },
            label = "example@exmaple.com",
            visible = true
        )

        MyButton(text = "보내기"){
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    viewModel.passwordReset(email)
                    onEnter(NavigationDestination.SignupComplete)
                } catch(e : HttpException){
                    makeError(context, e)
                } finally {
                    isLoading = false
                }
            }
        }

        if (isLoading) {
            Text(
                text = "로딩 중입니다...",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}