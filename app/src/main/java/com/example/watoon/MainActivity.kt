package com.example.watoon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.watoon.pages.MainPageBasic

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //우선 MainPageBasic을 사용하였지만 NavController를 이용하여 화면전환 하면될듯
            MainPageBasic()
        }
    }
}
