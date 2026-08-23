package com.example.cryptohub.presentation.screens.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.cryptohub.domain.models.Coin
import com.example.cryptohub.domain.models.ExchangeDetail
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class ExchangeDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = mockk<ExchangeDetailViewModel>(relaxed = true)

    @Test
    fun exchangeDetailScreen_displaysSuccessState() {
        // Given
        val dummyExchange = ExchangeDetail(
            id = 1,
            name = "Binance",
            logo = null,
            description = "The world's largest crypto exchange.",
            website = "https://binance.com",
            makerFee = 0.1,
            takerFee = 0.1,
            dateLaunched = "2017-07-01",
            currencies = listOf(
                Coin(1, "Bitcoin", "BTC", 50000.0)
            )
        )
        
        val uiState = ExchangeDetailUiState(
            isLoading = false,
            exchange = dummyExchange,
            error = null
        )
        
        every { viewModel.uiState } returns MutableStateFlow(uiState)

        // When
        composeTestRule.setContent {
            ExchangeDetailScreen(
                onNavigateBack = {},
                viewModel = viewModel
            )
        }

        // Then
        composeTestRule.onNodeWithText("Binance").assertIsDisplayed()
        composeTestRule.onNodeWithText("The world's largest crypto exchange.").assertIsDisplayed()
        composeTestRule.onNodeWithText("0.1%").assertIsDisplayed() // Maker Fee
        composeTestRule.onNodeWithText("Bitcoin").assertIsDisplayed()
    }
}
