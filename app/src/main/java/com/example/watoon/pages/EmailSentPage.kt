package com.example.watoon.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watoon.NavigationDestination
import com.example.watoon.viewModel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailSentPage (onEnter: (String) -> Unit){
    val viewModel: LoginViewModel = hiltViewModel()

    var email by rememberSaveable { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onEnter(NavigationDestination.Login)
                }
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Text(
                text = "비밀번호 찾기",
                modifier = Modifier.weight(1f)
            )
        }

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일 주소") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        MenuButton(text = "비밀번호 찾기"){
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    viewModel.passwordReset(email)
                    onEnter(NavigationDestination.SignupComplete)
                } catch(e : HttpException){
                    var message = ""
                    val errorBody = JSONObject(e.response()?.errorBody()?.string())
                    errorBody.keys().forEach { key ->
                        message += ("$key - ${errorBody.getString(key).substring(2 until errorBody.getString(key).length - 2)}" + "\n")
                    }
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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