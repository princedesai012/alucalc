package com.softellix.alucalc.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryFont,
    secondary = SecondaryColor,
    background = PrimaryDark,
    surface = PrimaryDark,
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryFont,         // #475569
    secondary = SecondaryColor,     // #94A3B8
    background = BackgroundGray,    // #F1F5F9
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = PrimaryFont,     // #475569
    onSurface = PrimaryFont         // #475569
)

@Composable
fun AluCalcTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep false to strictly enforce AluCalc custom palette
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
