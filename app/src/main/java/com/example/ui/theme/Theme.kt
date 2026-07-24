package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = PaciNavy,
    onPrimary = Color.White,
    primaryContainer = PaciSurfaceSoft,
    onPrimaryContainer = PaciNavy,
    secondary = PaciGold,
    onSecondary = PaciNavy,
    secondaryContainer = PaciBadgeBg,
    onSecondaryContainer = PaciBadgeText,
    tertiary = PaciGoldDark,
    background = PaciBackground,
    onBackground = PaciTextMain,
    surface = PaciSurface,
    onSurface = PaciTextMain,
    surfaceVariant = PaciSurfaceSoft,
    onSurfaceVariant = PaciTextMuted,
    outline = PaciBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = PaciGold,
    onPrimary = PaciNavy,
    primaryContainer = PaciNavyDark,
    onPrimaryContainer = Color.White,
    secondary = PaciGoldLight,
    onSecondary = PaciNavy,
    background = PaciNavyDark,
    onBackground = Color.White,
    surface = PaciNavyDark,
    onSurface = Color.White,
    surfaceVariant = PaciNavy,
    onSurfaceVariant = PaciTextMuted,
    outline = PaciNavyDark
)

@Composable
fun PaciFacilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Enforce PaciFácil brand palette
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
