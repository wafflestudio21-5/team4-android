package com.example.watoon.viewModel

import androidx.lifecycle.ViewModel
import com.example.watoon.MyApp
import com.example.watoon.data.LoginRequest
import com.example.watoon.data.PasswordResetRequest
import com.example.watoon.data.RegisterRequest
import com.example.watoon.function.setToken
import com.example.watoon.network.MyRestAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private var api : MyRestAPI) : ViewModel()  {

    suspend fun createAccount(email:String, pw1:String, pw2:String, nickname:String) {
        val registerRequest = RegisterRequest(email, pw1, pw2, nickname)
        api.createAccount(registerRequest)
    }

    suspend fun login(email:String, pw:String) {
        val loginRequest = LoginRequest(email, pw)

        val loginResponse = api.login(loginRequest)
        val token = loginResponse.access
        val id = loginResponse.user.id

        setToken(token, id.toString())
    }

    suspend fun passwordReset(email:String) {
        val passwordRequest = PasswordResetRequest(email)
        api.passwordReset(passwordRequest)
    }

    /*
    val loginRequest = LoginRequest(email, pw)

        val loginResponse = api.login(loginRequest)
        val token = loginResponse.access

        val body = loginResponse.body()
        if(body!=null) {
            val id = body.user.id
            MyApp.preferences.setToken("id", id.toString())
        }

        if (!loginResponse.isSuccessful) {
            val errorBody = body.toString()

           throw HttpException(Response.error<LoginResponse>(400 ,
               ResponseBody.create(
                   "plain/text".toMediaTypeOrNull(), // Specify the media type as needed
                   errorBody // Set your custom error message here
               )
            ))
        }

        val headers = loginResponse.headers().values("Set-Cookie")
        Log.d("headers", headers.toString())
        val tokens = headers?.map { it.substringBefore(';').trim() } ?: emptyList()
        val token = tokens.joinToString { "; " }
        MyApp.preferences.setToken("token", token)
        val id = loginResponse.user.id
        MyApp.preferences.setToken("id", id.toString())
     */
}