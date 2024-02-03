package com.example.watoon.pages

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watoon.NavigationDestination
import com.example.watoon.R
import com.example.watoon.function.MiniButton
import com.example.watoon.function.MyButton
import com.example.watoon.function.MyTextField
import com.example.watoon.function.makeError
import com.example.watoon.viewModel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
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

    val googleSignInClient: GoogleSignInClient by lazy { getGoogleClient(context) }
    val googleAuthLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("GoogleSignIn", "Callback invoked")
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                viewModel.googleLogin()
                Toast.makeText(context, "로그인 성공", Toast.LENGTH_LONG).show()
                onEnter(NavigationDestination.Main)
            } catch(e : HttpException){
                makeError(context, e)
            } finally {
                isLoading = false
            }
        }
    }

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

        MyButton(text = "로그인") {
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

        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(1.dp)){
            MiniButton(text = "구글 로그인") {
                /*googleSignInClient.signOut()
                val signInIntent = googleSignInClient.signInIntent
                Log.d("signinIntent", "passed")
                googleAuthLauncher.launch(signInIntent)*/
                Toast.makeText(context, "bug fix...", Toast.LENGTH_LONG).show()
            }
            MiniButton(text = "카카오 로그인") {
                Toast.makeText(context, "bug fix...", Toast.LENGTH_LONG).show()
            }
        }

        if (isLoading) {
            Text(
                text = "\n로딩 중입니다...",
                fontSize = 13.sp
            )
        }

        Text("\n\n")
    }
}

fun getGoogleClient(context : Context): GoogleSignInClient {
    val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestScopes(Scope("https://www.googleapis.com/auth/pubsub"))
        .requestServerAuthCode(clientID) // string 파일에 저장해둔 client id 를 이용해 server authcode를 요청한다.
        .requestEmail() // 이메일도 요청할 수 있다.
        .build()

    return GoogleSignIn.getClient(context, googleSignInOption)
}

private val clientID = "445263802217-3dcsa8llgeqh14i15gepc3u5j2s99ccl.apps.googleusercontent.com"
