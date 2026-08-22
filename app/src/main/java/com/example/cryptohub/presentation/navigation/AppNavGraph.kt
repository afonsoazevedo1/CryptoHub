package com.example.cryptohub.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.cryptohub.presentation.screens.detail.ExchangeDetailScreen
import com.example.cryptohub.presentation.screens.list.ExchangeListScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.ExchangeList
    ) {
        composable<Routes.ExchangeList> {
            ExchangeListScreen(
                onExchangeClick = { exchangeId ->
                    navController.navigate(Routes.ExchangeDetail(exchangeId))
                }
            )
        }

        composable<Routes.ExchangeDetail> {
            ExchangeDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}