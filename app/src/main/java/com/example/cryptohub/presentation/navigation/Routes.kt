package com.example.cryptohub.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {
    @Serializable
    data object ExchangeList : Routes

    @Serializable
    data class ExchangeDetail(
        val id: Int,
        val name: String,
        val spotVolumeUsd: Double,
        val dateLaunched: String?
    ) : Routes
}
