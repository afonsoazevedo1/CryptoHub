package com.example.cryptohub.data.repository

import android.util.Log
import com.example.cryptohub.core.Result
import com.example.cryptohub.core.error.ErrorType
import com.example.cryptohub.data.mapper.toCoinList
import com.example.cryptohub.data.mapper.toExchangeDetail
import com.example.cryptohub.data.mapper.toExchangeListItem
import com.example.cryptohub.data.remote.api.ExchangeAssetsResource
import com.example.cryptohub.data.remote.api.ExchangeInfoResource
import com.example.cryptohub.data.remote.api.ExchangeMapResource
import com.example.cryptohub.data.remote.dto.CoinListResponse
import com.example.cryptohub.data.remote.dto.ExchangeDetailResponse
import com.example.cryptohub.data.remote.dto.ExchangeListResponse
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.domain.models.ExchangeListItem
import com.example.cryptohub.domain.repository.ExchangeRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.time.Duration.Companion.milliseconds

class ExchangeRepositoryImpl(
    private val client: HttpClient,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExchangeRepository {

    companion object {
        private const val TAG = "ExchangeRepository"
        private const val MAX_RETRIES = 3
        private const val INITIAL_DELAY_MS = 100L
        private const val BACKOFF_MULTIPLIER = 2f
        private const val TIMEOUT_KEYWORD = "timeout"
    }

    override fun getExchanges(start: Int, limit: Int): Flow<Result<List<ExchangeListItem>>> = flow {
        emit(Result.Loading)

        // 1. Get the list of exchanges (ID, Name, etc)
        val mapResponse = retryWithExponentialBackoff {
            client.get(ExchangeMapResource(start = start, limit = limit))
                .body<ExchangeListResponse>()
        }
        
        val exchangeIds = mapResponse.data.joinToString(",") { it.id.toString() }
        
        // 2. Get detailed info (including volume) for all IDs in one batch
        val infoResponse = retryWithExponentialBackoff {
            client.get(ExchangeInfoResource(id = exchangeIds))
                .body<ExchangeDetailResponse>()
        }

        // 3. Merge data
        val exchanges = mapResponse.data.map { mapDto ->
            val detailDto = infoResponse.data[mapDto.id.toString()]
            
            // Strictly use spot_volume_usd if available from info response
            val volume = detailDto?.spotVolumeUsd 
                ?: mapDto.spotVolumeUsd 
                ?: detailDto?.volume24h 
                ?: 0.0
                
            mapDto.toExchangeListItem().copy(
                spotVolumeUsd = volume,
                logo = detailDto?.logo ?: mapDto.logo ?: "https://s2.coinmarketcap.com/static/img/exchanges/64x64/${mapDto.id}.png"
            )
        }

        emit(Result.Success(exchanges))
    }.handleErrors().flowOn(ioDispatcher)

    override fun getExchangeDetail(exchangeId: Int): Flow<Result<ExchangeDetail>> = flow {
        emit(Result.Loading)

        coroutineScope {
            val exchangeDeferred = async(ioDispatcher) {
                retryWithExponentialBackoff {
                    client.get(ExchangeInfoResource(id = exchangeId.toString()))
                        .body<ExchangeDetailResponse>()
                }
            }

            val coinsDeferred = async(ioDispatcher) {
                try {
                    retryWithExponentialBackoff {
                        client.get(ExchangeAssetsResource(id = exchangeId))
                            .body<CoinListResponse>()
                    }
                } catch (_: Exception) {
                    Log.w(TAG, "Failed to fetch coins for exchange $exchangeId")
                    null
                }
            }

            val exchangeResponse = exchangeDeferred.await()
            val coinsResponse = coinsDeferred.await()

            val exchangeDto = exchangeResponse.data[exchangeId.toString()]
                ?: exchangeResponse.data.values.firstOrNull()
                ?: error("Exchange $exchangeId not found")

            val coins = coinsResponse?.data?.toCoinList() ?: emptyList()
            val detail = exchangeDto.toExchangeDetail(coins)

            Log.d(TAG, "Loaded exchange ${detail.name} with ${coins.size} coins")
            emit(Result.Success(detail))
        }
    }.handleErrors().flowOn(Dispatchers.IO)

    private suspend inline fun <T> retryWithExponentialBackoff(
        crossinline block: suspend () -> T,
    ): T {
        var lastException: Exception? = null
        var delayMs = INITIAL_DELAY_MS

        repeat(MAX_RETRIES) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                lastException = e

                if (!isRetryable(e)) {
                    throw e
                }

                if (attempt < MAX_RETRIES - 1) {
                    Log.w(TAG, "Attempt ${attempt + 1} failed, retrying in ${delayMs}ms: ${e.message}")
                    delay(delayMs.milliseconds)
                    delayMs = (delayMs * BACKOFF_MULTIPLIER).toLong()
                }
            }
        }

        throw lastException ?: error("Failed after $MAX_RETRIES attempts")
    }

    private fun isRetryable(exception: Exception): Boolean = when (exception) {
        is IOException, is SocketTimeoutException -> true
        else -> exception.message?.contains(TIMEOUT_KEYWORD, ignoreCase = true) ?: false
    }

    private fun <T> Flow<Result<T>>.handleErrors(): Flow<Result<T>> = catch { e ->
        Log.e(TAG, "Error in repository: ${e.message}", e)
        val error = when (e) {
            is SocketTimeoutException -> ErrorType.TimeoutError(e.message ?: "Timeout")
            is IOException -> ErrorType.NetworkError(e.message ?: "Network error")
            is io.ktor.client.plugins.ResponseException -> {
                ErrorType.ServerError(e.response.status.value, e.message ?: "Server error")
            }
            else -> ErrorType.UnknownError(e.message ?: "Unknown error")
        }
        emit(Result.Error(error))
    }
}
