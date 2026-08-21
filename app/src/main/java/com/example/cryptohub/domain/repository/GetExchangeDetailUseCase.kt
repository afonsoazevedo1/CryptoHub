package com.example.cryptohub.domain.usecase

import com.example.cryptohub.core.Result
import com.example.cryptohub.domain.models.ExchangeDetail
import com.example.cryptohub.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangeDetailUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    operator fun invoke(exchangeId: Int): Flow<Result<ExchangeDetail>> {
        return repository.getExchangeDetail(exchangeId)
    }
}