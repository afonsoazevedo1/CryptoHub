package com.example.cryptohub.data.mapper

import com.example.cryptohub.data.remote.dto.CoinDto
import com.example.cryptohub.domain.models.Coin
import kotlin.collections.map

fun CoinDto.toCoin(): Coin {
    return Coin(
        id = id,
        name = name,
        symbol = symbol,
        priceUsd = priceUsd
    )
}

fun List<CoinDto>.toCoinList(): List<Coin> {
    return map { it.toCoin() }
}