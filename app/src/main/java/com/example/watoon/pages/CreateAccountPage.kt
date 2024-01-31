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
fun CreateAccountPage(onEnter: (String) -> Unit){
    val viewModel: LoginViewModel = hiltViewModel()

    var email by rememberSaveable { mutableStateOf("") }
    var pw1 by rememberSaveable { mutableStateOf("") }
    var pw2 by rememberSaveable { mutableStateOf("") }
    var nickname by rememberSaveable { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicTopBar(
            text = "회원가입",
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

        MyText(text = "비밀번호")
        MyTextField(
            value = pw1,
            onValueChange = { pw1 = it },
            label = "영어, 숫자 조합 9~16자리",
            visible = false
        )

        MyText(text = "비밀번호 확인")
        MyTextField(
            value = pw2,
            onValueChange = { pw2 = it },
            label = "영어, 숫자 조합 9~16자리",
            visible = false
        )

        MyText(text = "닉네임")
        MyTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = "",
            visible = false
        )

        MyButton(text = "회원가입"){
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    viewModel.createAccount(email, pw1, pw2, nickname)
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

