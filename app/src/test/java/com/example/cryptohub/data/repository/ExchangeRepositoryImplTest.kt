package com.example.cryptohub.data.repository

import android.util.Log
import app.cash.turbine.test
import com.example.cryptohub.core.Result
import com.example.cryptohub.core.error.ErrorType
import com.example.cryptohub.data.remote.dto.ExchangeDto
import com.example.cryptohub.data.remote.dto.ExchangeListResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ExchangeRepositoryImplTest {

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.e(any(), any<String>(), any()) } returns 0
        every { Log.d(any(), any<String>()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    private fun createMockClient(handler: suspend MockRequestHandleScope.(HttpRequestData) ->
    HttpResponseData): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler(handler)
            }
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(Resources)
        }
    }

    @Test
    fun `getExchanges should emit Success when API returns data`() = runTest {
        // Given
        val mapResponse = ExchangeListResponse(
            data = listOf(ExchangeDto(id = 1, name = "Binance"))
        )
        val infoResponse = com.example.cryptohub.data.remote.dto.ExchangeDetailResponse(
            data = mapOf(
                "1" to ExchangeDto(
                    id = 1, 
                    name = "Binance", 
                    spotVolumeUsd = 100.0,
                    logo = "logo_url"
                )
            )
        )
        
        val client = createMockClient { request ->
            val content = when {
                request.url.encodedPath.contains("map") -> Json.encodeToString(mapResponse)
                request.url.encodedPath.contains("info") -> Json.encodeToString(infoResponse)
                else -> error("Unhandled request: ${request.url}")
            }
            respond(
                content = content,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val repository = ExchangeRepositoryImpl(client, UnconfinedTestDispatcher(testScheduler))

        // When & Then
        repository.getExchanges(1, 20).test {
            assertEquals(Result.Loading, awaitItem())
            val result = awaitItem()
            assertTrue(result is Result.Success)
            val data = (result as Result.Success).data
            assertEquals("Binance", data[0].name)
            assertEquals(100.0, data[0].spotVolumeUsd, 0.0)
            assertEquals("logo_url", data[0].logo)
            awaitComplete()
        }
    }

    @Test
    fun `getExchanges should emit Error when API throws exception`() = runTest {
        // Given
        val client = createMockClient {
            throw IOException("Network fail")
        }
        val repository = ExchangeRepositoryImpl(client, UnconfinedTestDispatcher(testScheduler))

        // When & Then
        repository.getExchanges(1, 20).test {
            assertEquals(Result.Loading, awaitItem())
            val result = awaitItem()
            assertTrue(result is Result.Error)
            assertTrue((result as Result.Error).exception is ErrorType.NetworkError)
            awaitComplete()
        }
    }

    @Test
    fun `getExchangeDetail should emit Success when API returns data`() = runTest {
        // Given
        val infoResponse = com.example.cryptohub.data.remote.dto.ExchangeDetailResponse(
            data = mapOf(
                "1" to ExchangeDto(
                    id = 1, 
                    name = "Binance", 
                    description = "Desc",
                    logo = "logo_url",
                    spotVolumeUsd = 10000.0
                )
            )
        )
        
        val client = createMockClient {
            respond(
                content = Json.encodeToString(infoResponse),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val repository = ExchangeRepositoryImpl(client, UnconfinedTestDispatcher(testScheduler))

        // When & Then
        repository.getExchangeDetail(1).test {
            assertEquals(Result.Loading, awaitItem())
            val result = awaitItem()
            assertTrue(result is Result.Success)
            val data = (result as Result.Success).data
            assertEquals("Binance", data.name)
            assertEquals("Desc", data.description)
            assertEquals(10000.0, data.spotVolumeUsd!!, 0.0)
            awaitComplete()
        }
    }

    @Test
    fun `getExchanges should emit ServerError when API returns 500`() = runTest {
        // Given
        val client = createMockClient {
            respond(
                content = "Internal Server Error",
                status = HttpStatusCode.InternalServerError
            )
        }
        val repository = ExchangeRepositoryImpl(client, UnconfinedTestDispatcher(testScheduler))

        // When & Then
        repository.getExchanges(1, 20).test {
            assertEquals(Result.Loading, awaitItem())
            val result = awaitItem()
            assertTrue(result is Result.Error)
            val exception = (result as Result.Error).exception
            assertTrue(exception is ErrorType.ServerError)
            assertEquals(500, (exception as ErrorType.ServerError).code)
            awaitComplete()
        }
    }
}
