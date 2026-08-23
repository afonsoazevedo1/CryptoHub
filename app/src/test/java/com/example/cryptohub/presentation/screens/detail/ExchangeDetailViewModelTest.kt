package com.example.cryptohub.presentation.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.example.cryptohub.core.Result
import com.example.cryptohub.core.error.ErrorHandler
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.domain.usecase.GetExchangeDetailUseCase
import com.example.cryptohub.presentation.navigation.Routes
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
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
class ExchangeDetailViewModelTest {

    private val getExchangeDetailUseCase: GetExchangeDetailUseCase = mockk()
    private val errorHandler: ErrorHandler = mockk()
    private val savedStateHandle: SavedStateHandle = mockk()
    private lateinit var viewModel: ExchangeDetailViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Mocking toRoute extension function
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { savedStateHandle.toRoute<Routes.ExchangeDetail>() } returns
                Routes.ExchangeDetail(1, "Binance", 100.0, "2017")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadExchangeDetail should update state with Success`() = runTest {
        // Given
        val detail = ExchangeDetail(1, "Binance", null, null, null, null, null, null, emptyList())
        every { getExchangeDetailUseCase(1) } returns flowOf(
            Result.Loading,
            Result.Success(detail)
        )

        // When
        viewModel = ExchangeDetailViewModel(getExchangeDetailUseCase, errorHandler, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(detail, state.exchange)
            assertEquals(false, state.isLoading)
        }
    }
}
