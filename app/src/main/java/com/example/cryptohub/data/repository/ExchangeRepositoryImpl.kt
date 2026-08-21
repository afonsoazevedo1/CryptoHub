package com.example.cryptohub.data.repository

import com.example.cryptohub.core.Result
import com.example.cryptohub.core.error.ErrorType
import com.example.cryptohub.data.mapper.toCoin
import com.example.cryptohub.data.mapper.toCoinList
import com.example.cryptohub.data.mapper.toExchangeDetail
import com.example.cryptohub.data.mapper.toExchangeListItem
import com.example.cryptohub.data.remote.FakeRemoteDataSource
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.domain.models.ExchangeListItem
import com.example.cryptohub.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor() : ExchangeRepository {

    override fun getExchanges(): Flow<Result<List<ExchangeListItem>>> = flow {
        try {
            emit(Result.Loading)
            val response = FakeRemoteDataSource.getExchangesList()
            val exchanges = response.data.values.map { it.toExchangeListItem() }
            emit(Result.Success(exchanges))
        } catch (e: Exception) {
            emit(Result.Error(ErrorType.UnknownError(e.message ?: "Erro ao carregar exchanges")))
        }
    }

    override fun getExchangeDetail(exchangeId: Int): Flow<Result<ExchangeDetail>> = flow {
        try {
            emit(Result.Loading)
            val exchangeResponse = FakeRemoteDataSource.getExchangeDetail(exchangeId)
            val coinsResponse = FakeRemoteDataSource.getExchangeCoins(exchangeId)

            val coins = coinsResponse.data.toCoinList()
            val detail = exchangeResponse.data.toExchangeDetail(coins)

            emit(Result.Success(detail))
        } catch (e: Exception) {
            emit(Result.Error(ErrorType.UnknownError(e.message ?: "Erro ao carregar detalhes")))
        }
    }
}