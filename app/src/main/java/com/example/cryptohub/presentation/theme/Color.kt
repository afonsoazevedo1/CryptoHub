package com.example.cryptohub.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White

// Base Colors - Named Constants
private const val HEX_CRYPTO_BLACK = 0xFF0B0E11
private const val HEX_CRYPTO_DARK_GRAY = 0xFF181A20
private const val HEX_CRYPTO_GREEN = 0xFF0ECB81
private const val HEX_CRYPTO_BLUE = 0xFF4788FF
private const val HEX_CRYPTO_TEXT_GRAY = 0xFF848E9C
private const val HEX_CRYPTO_WHITE = 0xFFEAECEF
private const val HEX_CRYPTO_LIGHT_SURFACE = 0xFFF5F5F5
private const val HEX_CRYPTO_OUTLINE = 0xFF323539

val CryptoBlack = Color(HEX_CRYPTO_BLACK)
val CryptoDarkGray = Color(HEX_CRYPTO_DARK_GRAY)
val CryptoGreen = Color(HEX_CRYPTO_GREEN)
val CryptoBlue = Color(HEX_CRYPTO_BLUE)
val CryptoTextGray = Color(HEX_CRYPTO_TEXT_GRAY)
val CryptoWhite = Color(HEX_CRYPTO_WHITE)

val md_theme_light_primary = CryptoBlue
val md_theme_light_onPrimary = White
val md_theme_light_background = White
val md_theme_light_surface = Color(HEX_CRYPTO_LIGHT_SURFACE)
val md_theme_light_onSurface = CryptoBlack

val md_theme_dark_primary = CryptoBlue
val md_theme_dark_onPrimary = White
val md_theme_dark_secondary = CryptoGreen
val md_theme_dark_background = CryptoBlack
val md_theme_dark_surface = CryptoDarkGray
val md_theme_dark_onSurface = CryptoWhite
val md_theme_dark_onSurfaceVariant = CryptoTextGray
val md_theme_dark_outline = Color(HEX_CRYPTO_OUTLINE)
