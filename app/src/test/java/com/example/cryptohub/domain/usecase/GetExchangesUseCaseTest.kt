package com.example.cryptohub.domain.usecase

import app.cash.turbine.test
import com.example.cryptohub.core.Result
import com.example.cryptohub.domain.models.ExchangeListItem
import com.example.cryptohub.domain.repository.ExchangeRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetExchangesUseCaseTest {

    private lateinit var useCase: GetExchangesUseCase
    private val repository: ExchangeRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetExchangesUseCase(repository)
    }

    @Test
    fun `invoke should call repository and return result`() = runTest {
        val exchanges = listOf(
            ExchangeListItem(1, "Binance", "logo", 1000.0, "date")
        )
        val expectedResult = Result.Success(exchanges)
        
        every { repository.getExchanges(1, 20) } returns flowOf(expectedResult)

        useCase().test {
            assertEquals(expectedResult, awaitItem())
            awaitComplete()
        }
    }
}
