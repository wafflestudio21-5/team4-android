package com.example.watoon.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watoon.NavigationDestination
import com.example.watoon.R
import com.example.watoon.function.LoginButton
import com.example.watoon.function.MenuButton
import com.example.watoon.function.MyTextField
import com.example.watoon.function.makeError
import com.example.watoon.network.MyRestAPI
import com.example.watoon.viewModel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
                contentDescription = "",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        onEnter(NavigationDestination.Login)
                    },
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                text = "회원가입",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
            Image(
                painter = painterResource(R.drawable.baseline_arrow_back_ios_new_24),
                contentDescription = "",
                modifier = Modifier.size(30.dp),
                colorFilter = ColorFilter.tint(Color.Black)
            )
        }

        CreateAccountText(text = "\n이메일 주소")
        MyTextField(
            value = email,
            onValueChange = { email = it },
            label = "example@exmaple.com",
            visible = true
        )

        CreateAccountText(text = "비밀번호")
        MyTextField(
            value = pw1,
            onValueChange = { pw1 = it },
            label = "영어, 숫자 조합 9~16자리",
            visible = false
        )

        CreateAccountText(text = "비밀번호 확인")
        MyTextField(
            value = pw2,
            onValueChange = { pw2 = it },
            label = "영어, 숫자 조합 9~16자리",
            visible = false
        )

        CreateAccountText(text = "닉네임")
        MyTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = "",
            visible = false
        )

        LoginButton(text = "회원가입"){
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
            Text("로딩 중입니다...")
        }
    }
}

@Composable
fun CreateAccountText(text:String){
    Text(
        text = text,
        modifier = Modifier.padding(10.dp),
        fontWeight = FontWeight.Bold
    )
}