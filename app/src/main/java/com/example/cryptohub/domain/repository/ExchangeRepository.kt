package com.example.cryptohub.domain.repository

import com.example.cryptohub.core.Result
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.domain.models.ExchangeListItem
import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {
    fun getExchanges(start: Int, limit: Int): Flow<Result<List<ExchangeListItem>>>
    fun getExchangeDetail(exchangeId: Int): Flow<Result<ExchangeDetail>>
}
