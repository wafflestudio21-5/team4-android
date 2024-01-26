package com.example.watoon

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application(){
    companion object {
        lateinit var preferences : MySharedPreferences
    }

    override fun onCreate() {
        preferences = MySharedPreferences(applicationContext)
        super.onCreate()
    }
}