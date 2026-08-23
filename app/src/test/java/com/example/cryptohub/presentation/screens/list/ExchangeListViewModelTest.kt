package com.example.cryptohub.presentation.screens.list

import app.cash.turbine.test
import com.example.cryptohub.core.Result
import com.example.cryptohub.core.error.ErrorHandler
import com.example.cryptohub.core.error.ErrorType
import com.example.cryptohub.domain.models.ExchangeListItem
import com.example.cryptohub.domain.usecase.GetExchangesUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExchangeListViewModelTest {

    private val getExchangesUseCase: GetExchangesUseCase = mockk()
    private val errorHandler: ErrorHandler = mockk()
    private lateinit var viewModel: ExchangeListViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadExchanges should update uiState with success when use case returns data`() = runTest {
        // Given
        val exchanges = listOf(
            ExchangeListItem(1, "Binance", null, 1000.0, "2017-07-14")
        )
        every { getExchangesUseCase(any(), any()) } returns flowOf(
            Result.Loading,
            Result.Success(exchanges)
        )

        // When
        viewModel = ExchangeListViewModel(getExchangesUseCase, errorHandler)

        // Then
        viewModel.uiState.test {
            // O estado inicial padrão (isLoading = false)
            val state1 = awaitItem()
            assertEquals(false, state1.isLoading)

            testDispatcher.scheduler.advanceUntilIdle()

            // O estado após Result.Loading (isLoading = true)
            val state2 = awaitItem()
            assertEquals(true, state2.isLoading)

            // O estado após Result.Success (isLoading = false)
            val state3 = awaitItem()
            assertEquals(false, state3.isLoading)
            assertEquals(exchanges, state3.exchanges)
        }
    }

    @Test
    fun `loadExchanges should update uiState with error when use case returns error`() = runTest {
        // Given
        val errorType = ErrorType.NetworkError("No Internet")
        val errorMessage = "Falha de conexão"

        every { getExchangesUseCase(any(), any()) } returns flowOf(
            Result.Loading,
            Result.Error(errorType)
        )
        every { errorHandler.getErrorMessage(errorType) } returns errorMessage

        // When
        viewModel = ExchangeListViewModel(getExchangesUseCase, errorHandler)

        // Then
        viewModel.uiState.test {
            val state1 = awaitItem()
            assertEquals(false, state1.isLoading)

            testDispatcher.scheduler.advanceUntilIdle()

            val state2 = awaitItem()
            assertEquals(true, state2.isLoading)

            val state3 = awaitItem()
            assertEquals(false, state3.isLoading)
            assertEquals(errorMessage, state3.error)
        }
    }
}
