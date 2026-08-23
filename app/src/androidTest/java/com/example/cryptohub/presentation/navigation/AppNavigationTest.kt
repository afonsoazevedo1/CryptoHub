package com.example.cryptohub.presentation.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.cryptohub.domain.models.ExchangeListItem
import com.example.cryptohub.presentation.screens.detail.ExchangeDetailUiState
import com.example.cryptohub.presentation.screens.detail.ExchangeDetailViewModel
import com.example.cryptohub.presentation.screens.list.ExchangeListUiState
import com.example.cryptohub.presentation.screens.list.ExchangeListViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class AppNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private val listViewModel = mockk<ExchangeListViewModel>(relaxed = true)
    private val detailViewModel = mockk<ExchangeDetailViewModel>(relaxed = true)

    @Before
    fun setup() {
        stopKoin() // Ensure a clean state
        startKoin {
            modules(module {
                viewModel { listViewModel }
                viewModel { detailViewModel }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun appNavGraph_startDestination_isExchangeList() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            AppNavGraph(navController = navController)
        }

        assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<Routes.ExchangeList>() == true)
    }

    @Test
    fun clickingExchange_navigatesToDetail() {
        // Given
        val dummyExchanges = listOf(
            ExchangeListItem(1, "Binance", null, 1000000.0, "2017-07-01")
        )
        every { listViewModel.uiState } returns MutableStateFlow(ExchangeListUiState(exchanges = dummyExchanges))
        every { detailViewModel.uiState } returns MutableStateFlow(ExchangeDetailUiState(isLoading = true))

        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            AppNavGraph(navController = navController)
        }

        // When
        composeTestRule.onNodeWithText("Binance").performClick()

        // Then
        assertTrue(navController.currentBackStackEntry?.destination?.hasRoute<Routes.ExchangeDetail>() == true)
    }
}
