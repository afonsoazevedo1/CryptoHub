package com.example.cryptohub.presentation.screens.list

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.example.cryptohub.domain.models.ExchangeListItem
import com.example.cryptohub.presentation.theme.CryptoHubTheme
import org.junit.Rule
import org.junit.Test

class ExchangeListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun exchangeList_showsItems() {
        val exchanges = listOf(
            ExchangeListItem(1, "Binance", null, 1000.0, "2017"),
            ExchangeListItem(2, "Coinbase", null, 500.0, "2012")
        )

        composeTestRule.setContent {
            CryptoHubTheme {
                ExchangesList(
                    exchanges = exchanges,
                    onExchangeClick = {},
                    isLoadingMore = false,
                    endReached = false,
                    onLoadMore = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Binance").assertIsDisplayed()
        composeTestRule.onNodeWithText("Coinbase").assertIsDisplayed()
    }
}
