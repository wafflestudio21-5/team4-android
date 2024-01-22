package com.example.watoon

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences (context: Context){
    private val preferences: SharedPreferences = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getToken(key: String, defValue: String):String{
        return preferences.getString(key,defValue).toString()
    }

    fun setToken(key: String, defValue: String){
        preferences.edit().putString(key, defValue).apply()
    }

    fun removeToken(key: String){
        preferences.edit().remove(key).apply()
    }
}