package com.example.cryptohub.data.repository

import com.example.cryptohub.core.Result
import com.example.cryptohub.core.error.ErrorType
import com.example.cryptohub.data.mapper.toCoinList
import com.example.cryptohub.data.mapper.toExchangeDetail
import com.example.cryptohub.data.mapper.toExchangeListItem
import com.example.cryptohub.data.remote.api.CoinMarketCapApi
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.domain.models.ExchangeListItem
import com.example.cryptohub.domain.repository.ExchangeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val api: CoinMarketCapApi
) : ExchangeRepository {

    override fun getExchanges(): Flow<Result<List<ExchangeListItem>>> = flow {
        emit(Result.Loading)
        val response = api.getExchanges()
        val exchanges = response.data.values.map { it.toExchangeListItem() }
        emit(Result.Success(exchanges))
    }.handleErrors().flowOn(Dispatchers.IO)

    override fun getExchangeDetail(exchangeId: Int): Flow<Result<ExchangeDetail>> = flow {
        emit(Result.Loading)
        val exchangeResponse = api.getExchangeDetail(exchangeId)
        val coinsResponse = api.getExchangeCoins(exchangeId)

        val coins = coinsResponse.data.toCoinList()
        val detail = exchangeResponse.data.toExchangeDetail(coins)

        emit(Result.Success(detail))
    }.handleErrors().flowOn(Dispatchers.IO)

    private fun <T> Flow<Result<T>>.handleErrors(): Flow<Result<T>> = catch { e ->
        val error = when (e) {
            is IOException -> ErrorType.NetworkError(e.message ?: "")
            is HttpException -> ErrorType.ServerError(e.code(), e.message())
            else -> ErrorType.UnknownError(e.message ?: "")
        }
        emit(Result.Error(error))
    }
}