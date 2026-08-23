package com.example.cryptohub.domain.usecase

import app.cash.turbine.test
import com.example.cryptohub.core.Result
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.domain.repository.ExchangeRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetExchangeDetailUseCaseTest {

    private lateinit var useCase: GetExchangeDetailUseCase
    private val repository: ExchangeRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetExchangeDetailUseCase(repository)
    }

    @Test
    fun `invoke should call repository and return result`() = runTest {
        val detail = ExchangeDetail(1, "Binance", "logo", "desc", "web", 0.1, 0.2, "date")
        val expectedResult = Result.Success(detail)
        
        every { repository.getExchangeDetail(1) } returns flowOf(expectedResult)

        useCase(1).test {
            assertEquals(expectedResult, awaitItem())
            awaitComplete()
        }
    }
}
