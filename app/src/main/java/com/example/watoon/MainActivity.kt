package com.example.watoon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.watoon.pages.CreateAccountPage
import com.example.watoon.pages.EmailSentPage
import com.example.watoon.pages.LoginPage
import com.example.watoon.pages.MainPage
import com.example.watoon.pages.MainPageBasic
import com.example.watoon.pages.SearchPage
import com.example.watoon.pages.WebtoonUploadPage
import com.example.watoon.ui.theme.WatoonTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatoonTheme {
                SetNavigation()
            }
        }
    }
}

@Composable
private fun SetNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationDestination.Login) {
        composable(NavigationDestination.Login) {
            LoginPage(onEnter = { navController.navigate(it) })
        }
        composable(NavigationDestination.Main) {
            MainPageBasic(onEnter = { navController.navigate(it) })
        }
        composable(NavigationDestination.CreateAccount) {
            CreateAccountPage(onEnter = { navController.navigate(it) })
        }
        composable(NavigationDestination.EmailSent){
            EmailSentPage(onEnter = { navController.navigate(it) })
        }
        composable(NavigationDestination.Search){
            SearchPage(onEnter = {navController.navigate(it)})
        }
        composable(NavigationDestination.WebtoonUpload){
            WebtoonUploadPage(onEnter = {navController.navigate(it)})
        }
    }
}


