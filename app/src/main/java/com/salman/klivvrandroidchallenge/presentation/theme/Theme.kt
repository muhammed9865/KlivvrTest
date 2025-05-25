package com.salman.klivvrandroidchallenge.presentation.theme

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
    primary = AccentBlue,
    onPrimary = Color.White,
    primaryContainer = AccentBlue.copy(alpha = 0.1f),
    onPrimaryContainer = AccentBlue,

    secondary = TextSecondary,
    onSecondary = Color.White,
    secondaryContainer = SearchBarGray,
    onSecondaryContainer = TextSecondary,

    background = BackgroundGray,
    onBackground = TextPrimary,

    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = SearchBarGray,
    onSurfaceVariant = TextSecondary,

    surfaceTint = AccentBlue,
    inverseSurface = TextPrimary,
    inverseOnSurface = SurfaceWhite,

    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRed.copy(alpha = 0.1f),
    onErrorContainer = ErrorRed,

    outline = DividerGray,
    outlineVariant = DividerGray.copy(alpha = 0.5f),

    scrim = Color.Black.copy(alpha = 0.32f)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4A9EFF), // Lighter blue for dark theme
    onPrimary = Color(0xFF001E3C),
    primaryContainer = Color(0xFF1B3A5C),
    onPrimaryContainer = Color(0xFFB8D4FF),

    secondary = Color(0xFFBBBBBB), // Light gray for secondary elements
    onSecondary = Color(0xFF1C1C1C),
    secondaryContainer = Color(0xFF2F2F2F), // Dark card background
    onSecondaryContainer = Color(0xFFE0E0E0),

    tertiary = Color(0xFFFF5252), // Vibrant red for flags in dark mode
    onTertiary = Color(0xFF3C0A00),
    tertiaryContainer = Color(0xFF5C1900),
    onTertiaryContainer = Color(0xFFFFB3B3),

    background = Color(0xFF0F0F0F), // Very dark background
    onBackground = Color(0xFFE8E8E8), // Light text on dark background

    surface = Color(0xFF1A1A1A), // Dark surface for cards/list items
    onSurface = Color(0xFFE8E8E8), // Light text on dark surface
    surfaceVariant = Color(0xFF252525), // Search bar dark background
    onSurfaceVariant = Color(0xFFB8B8B8), // Coordinate text in dark mode

    surfaceTint = Color(0xFF4A9EFF),
    inverseSurface = Color(0xFFE8E8E8),
    inverseOnSurface = Color(0xFF1A1A1A),

    error = Color(0xFFFF6B6B), // Softer red for dark theme
    onError = Color(0xFF3C0A0A),
    errorContainer = Color(0xFF5C1414),
    onErrorContainer = Color(0xFFFFB3B3),

    outline = Color(0xFF3D3D3D), // Subtle dividers
    outlineVariant = Color(0xFF2A2A2A),

    scrim = Color.Black.copy(alpha = 0.6f)
)

@Composable
fun KlivvrAndroidChallengeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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