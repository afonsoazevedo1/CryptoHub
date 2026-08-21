package com.example.cryptohub.domain.usecase

import com.example.cryptohub.core.Result
import com.example.cryptohub.domain.models.ExchangeListItem
import com.example.cryptohub.domain.repository.ExchangeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExchangesUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    operator fun invoke(): Flow<Result<List<ExchangeListItem>>> {
        return repository.getExchanges()
    }
}