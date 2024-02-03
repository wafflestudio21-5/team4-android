package com.example.watoon.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watoon.NavigationDestination
import com.example.watoon.function.BasicTopBar
import com.example.watoon.function.MyButton
import com.example.watoon.function.MyText
import com.example.watoon.function.MyTextField
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.LoginViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

@Composable
fun NicknameChangePage(
    onEnter: (String) -> Unit,
    ){
    val viewModel: LoginViewModel = hiltViewModel()

    var nickname by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
      modifier = Modifier
          .fillMaxWidth()
    ) {
        BasicTopBar(
            text = "닉네임 바꾸기",
            onEnter = onEnter,
            destination = NavigationDestination.Main
        )

        MyText(text = "닉네임")

        MyTextField(
            value = nickname,
            onValueChange = {nickname = it},
            label = "닉네임을 입력하세요",
            visible = true
        )

        MyButton(
            text = "확인"
        ) {
            scope.launch {
                try {
                    viewModel.changeNickname(nickname)
                }catch (e:HttpException){
                    makeError(context, e)
                }
            }
        }
    }

}