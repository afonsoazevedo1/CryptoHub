package com.example.cryptohub.domain.models

data class Exchange(
    val id: Int,
    val name: String,
    val logo: String?,
    val spotVolumeUsd: Double,
    val dateLaunched: String?
)