package com.example.cryptohub.data.mapper

import com.example.cryptohub.data.remote.dto.ExchangeAssetDto
import com.example.cryptohub.domain.models.Coin

fun ExchangeAssetDto.toCoin(): Coin {
    return Coin(
        id = currency.id ?: 0,
        name = currency.name,
        symbol = currency.symbol ?: "",
        priceUsd = currency.priceUsd ?: quote?.get("USD")?.price
    )
}

fun List<ExchangeAssetDto>.toCoinList(): List<Coin> {
    return map { it.toCoin() }
}
