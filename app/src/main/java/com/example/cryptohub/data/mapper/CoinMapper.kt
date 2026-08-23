package com.example.cryptohub.data.mapper

import com.example.cryptohub.data.remote.dto.ExchangeAssetDto
import com.example.cryptohub.domain.models.Coin

fun ExchangeAssetDto.toCoin(): Coin {
    return Coin(
        id = currency.id,
        name = currency.name,
        symbol = currency.symbol,
        priceUsd = quote?.get("USD")?.price
    )
}

fun List<ExchangeAssetDto>.toCoinList(): List<Coin> {
    return map { it.toCoin() }
}
