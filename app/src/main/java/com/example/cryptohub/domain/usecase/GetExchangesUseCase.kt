package com.example.cryptohub.domain.usecase

import com.example.cryptohub.core.Result
import com.example.cryptohub.domain.models.ExchangeListItem
import com.example.cryptohub.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
class GetExchangesUseCase(
    private val repository: ExchangeRepository
) {
    operator fun invoke(start: Int = 1, limit: Int = 20): Flow<Result<List<ExchangeListItem>>> {
        return repository.getExchanges(start, limit)
    }
}
