package com.example.cryptohub.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cryptohub.presentation.screens.detail.ExchangeDetailScreen
import com.example.cryptohub.presentation.screens.list.ExchangeListScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            ExchangeListScreen(
                onExchangeClick = { exchangeId ->
                    navController.navigate("detail/$exchangeId")
                }
            )
        }

        composable(
            route = "detail/{exchangeId}",
            arguments = listOf(
                androidx.navigation.navArgument("exchangeId") {
                    type = androidx.navigation.NavType.IntType
                }
            )
        ) { backStackEntry ->
            ExchangeDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}