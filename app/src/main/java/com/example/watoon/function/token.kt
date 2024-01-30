package com.example.watoon.function

import com.example.watoon.MyApp

fun setToken(token:String, id:String){
    MyApp.preferences.setToken("token", token)
    MyApp.preferences.setToken("id", id)
}
fun getToken():String{
    return "access=" + MyApp.preferences.getToken("token", "")
}