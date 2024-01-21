package com.example.watoon.viewModel

import androidx.lifecycle.ViewModel
import com.example.watoon.data.LoginRequest
import com.example.watoon.data.RegisterRequest
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
        api.login(loginRequest)
    }

}