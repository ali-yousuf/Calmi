package com.calmi.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CalmPrimary,
    secondary = CalmSecondary,
    tertiary = CalmTertiary,
    background = CalmBackground,
    surface = CalmSurface,
    onPrimary = Color.White,
    onSecondary = CalmTextPrimary,
    onBackground = CalmTextPrimary,
    onSurface = CalmTextPrimary,
    error = CalmError
)

@Composable
fun CalmiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = CalmiTypography,
        content = content
    )
}
