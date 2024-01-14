package com.example.watoon

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            MainPage()
        }
        composable(NavigationDestination.CreateAccount) {
            CreateAccountPage(onEnter = { navController.navigate(it) })
        }
        composable(NavigationDestination.EmailSent){
            EmailSentPage(onEnter = { navController.navigate(it) })
        }
    }
}


/*@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
    WatoonTheme {
        LoginPage(modifier = Modifier.fillMaxSize())
    }
}*/