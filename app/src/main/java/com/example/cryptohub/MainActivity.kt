package com.example.cryptohub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.example.cryptohub.presentation.navigation.AppNavGraph
import com.example.cryptohub.presentation.theme.CryptoHubTheme
import com.example.cryptohub.presentation.theme.ThemePreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themePreference: ThemePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode = themePreference.isDarkModeFlow.collectAsState(initial = isSystemInDarkTheme())
            val navController = rememberNavController()

            CryptoHubTheme(darkTheme = isDarkMode.value) {
                AppNavGraph(navController = navController)
            }
        }
    }
}