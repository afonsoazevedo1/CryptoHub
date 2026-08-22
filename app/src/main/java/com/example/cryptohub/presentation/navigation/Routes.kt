package com.example.cryptohub.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object ExchangeList : Routes

    @Serializable
    data class ExchangeDetail(val exchangeId: Int) : Routes
}