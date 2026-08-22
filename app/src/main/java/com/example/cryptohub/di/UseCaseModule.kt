package com.example.cryptohub.di

import com.example.cryptohub.domain.repository.ExchangeRepository
import com.example.cryptohub.domain.usecase.GetExchangesUseCase
import com.example.cryptohub.domain.usecase.GetExchangeDetailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetExchangesUseCase(
        repository: ExchangeRepository
    ): GetExchangesUseCase {
        return GetExchangesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetExchangeDetailUseCase(
        repository: ExchangeRepository
    ): GetExchangeDetailUseCase {
        return GetExchangeDetailUseCase(repository)
    }
}