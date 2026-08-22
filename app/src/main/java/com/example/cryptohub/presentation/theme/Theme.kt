package com.example.cryptohub.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFA3C9FF),
    onPrimary = Color(0xFF003A70),
    primaryContainer = Color(0xFF00509E),
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondary = md_accent_gold_light,
    onSecondary = Color(0xFF3F2E0D),
    secondaryContainer = md_accent_gold_dark,
    onSecondaryContainer = Color(0xFFF4E4C1),
    tertiary = Color(0xFFD5BFFF),
    onTertiary = Color(0xFF3B2A4D),
    tertiaryContainer = Color(0xFF534167),
    onTertiaryContainer = Color(0xFFF2DAFF),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E6),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E6),
    surfaceVariant = Color(0xFF49454E),
    onSurfaceVariant = Color(0xFFCAC7D0),
    outline = Color(0xFF938F99),
    inverseOnSurface = Color(0xFF1C1B1F),
    inverseSurface = Color(0xFFE6E1E6),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006DB3),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD1E4FF),
    onPrimaryContainer = Color(0xFF001D3B),
    secondary = md_accent_gold,
    onSecondary = Color(0xFF3F2E0D),
    secondaryContainer = md_accent_gold_light,
    onSecondaryContainer = Color(0xFF251700),
    tertiary = Color(0xFF6B5B7E),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF2DAFF),
    onTertiaryContainer = Color(0xFF251635),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    background = Color(0xFFFEFBFF),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFEFBFF),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454E),
    outline = Color(0xFF79747E),
    inverseOnSurface = Color(0xFFF4EFF4),
    inverseSurface = Color(0xFF313033),
)

@Composable
fun CryptoHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}