package com.example.cryptohub.domain.usecase

import com.example.cryptohub.core.Result
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
class GetExchangeDetailUseCase(
    private val repository: ExchangeRepository
) {
    operator fun invoke(exchangeId: Int): Flow<Result<ExchangeDetail>> {
        return repository.getExchangeDetail(exchangeId)
    }
}
