package com.example.watoon.function

import android.content.Context
import android.widget.Toast
import org.json.JSONObject
import retrofit2.HttpException

fun makeError(context: Context, e: HttpException){
    var message = ""
    val errorBody = JSONObject(e.response()?.errorBody()?.string())
    errorBody.keys().forEach { key ->
        message += ("$key - ${errorBody.getString(key).substring(2 until errorBody.getString(key).length - 2)}" + "\n")
    }
    message = message.substring(0, message.length-1)
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}