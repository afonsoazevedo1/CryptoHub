package com.example.cryptohub.di

import com.example.cryptohub.data.repository.ExchangeRepositoryImpl
import com.example.cryptohub.domain.repository.ExchangeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindExchangeRepository(
        impl: ExchangeRepositoryImpl
    ): ExchangeRepository
}