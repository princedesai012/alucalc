package com.alucalc.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colors matched to the Figma design: near-black primary, white surfaces, light-gray backgrounds
val AluBlack = Color(0xFF1A1A1A)
val AluDarkSurface = Color(0xFF15161A)
val AluBackground = Color(0xFFF7F7F8)
val AluCardBorder = Color(0xFFE7E7E9)
val AluTextSecondary = Color(0xFF8A8A8E)
val AluTextTertiary = Color(0xFFB5B5B9)

private val AluColorScheme = lightColorScheme(
    primary = AluBlack,
    onPrimary = Color.White,
    background = AluBackground,
    surface = Color.White,
    onSurface = AluBlack,
    onBackground = AluBlack,
    surfaceVariant = AluBackground,
    outline = AluCardBorder
)

@Composable
fun AluCalcTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AluColorScheme,
        typography = AluTypography,
        content = content
    )
}
