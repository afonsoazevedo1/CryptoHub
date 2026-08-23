package com.example.cryptohub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.example.cryptohub.presentation.navigation.AppNavGraph
import com.example.cryptohub.presentation.theme.CryptoHubTheme
import com.example.cryptohub.presentation.theme.ThemePreference
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val themePreference: ThemePreference by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val systemInDarkTheme = isSystemInDarkTheme()
            val isDarkModePref = themePreference.isDarkModeFlow.collectAsState(initial = null)

            // Se a preferência for nula, obedece ao sistema. Caso contrário, usa a preferência.
            val darkTheme = isDarkModePref.value ?: systemInDarkTheme
            val navController = rememberNavController()

            CryptoHubTheme(darkTheme = darkTheme) {
                AppNavGraph(navController = navController)
            }
        }
    }
}
