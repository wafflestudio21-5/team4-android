package com.example.watoon.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.watoon.data.CustomRegister
import com.example.watoon.network.MyRestAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private var api : MyRestAPI) : ViewModel()  {

    suspend fun createAccount(email:String, pw1:String, pw2:String, nickname:String) : CustomRegister {
        val customRegister = CustomRegister(email, pw1, pw2, nickname)
        val response = api.createAccount(customRegister)
        return response
    }

}