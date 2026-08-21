package com.example.cryptohub.domain.models

import kotlin.collections.emptyList

data class ExchangeDetail(
    val id: Int,
    val name: String,
    val logo: String?,
    val description: String?,
    val website: String?,
    val makerFee: Double?,
    val takerFee: Double?,
    val dateLaunched: String?,
    val currencies: List<Coin> = emptyList()
)
