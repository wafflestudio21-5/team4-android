package com.example.watoon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.watoon.pages.CreateAccountPage
import com.example.watoon.pages.EmailSentPage
import com.example.watoon.pages.EpisodeUploadPage
import com.example.watoon.pages.LoginPage
import com.example.watoon.pages.MainPageBasic
import com.example.watoon.pages.NewWebtoonPage
import com.example.watoon.pages.SearchPage
import com.example.watoon.pages.SignupCompletePage
import com.example.watoon.pages.WebtoonUploadPage
import com.example.watoon.ui.theme.WatoonTheme
import com.example.watoon.viewModel.UploadViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
    val uploadViewModel: UploadViewModel = hiltViewModel()

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
        composable(NavigationDestination.SignupComplete){
            SignupCompletePage(onEnter = { navController.navigate(it) })
        }
        composable(NavigationDestination.Search){
            SearchPage(uploadViewModel, onEnter = {navController.navigate(it)})
        }
        composable(NavigationDestination.WebtoonUpload){
            WebtoonUploadPage(uploadViewModel, onEnter = {navController.navigate(it)})
        }
        composable(NavigationDestination.EpisodeUpload){
            EpisodeUploadPage(uploadViewModel, onEnter = {navController.navigate(it)})
        }
        composable(NavigationDestination.NewWebtoon){
            NewWebtoonPage(uploadViewModel, onEnter = {navController.navigate(it)})
        }
    }
}


