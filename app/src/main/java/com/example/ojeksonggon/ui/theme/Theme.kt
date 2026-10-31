package com.example.ojeksonggon.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = SonggonGreenPrimary,
    onPrimary = SonggonSurface,
    primaryContainer = SonggonGreenContainer,
    onPrimaryContainer = SonggonOnGreenContainer,
    secondary = SonggonAmber,
    onSecondary = SonggonSurface,
    secondaryContainer = SonggonAmberContainer,
    onSecondaryContainer = SonggonOnAmberContainer,
    background = SonggonBackground,
    onBackground = SonggonSlateDark,
    surface = SonggonSurface,
    onSurface = SonggonSlateDark,
    surfaceVariant = SonggonSurfaceVariant,
    onSurfaceVariant = SonggonSlateMedium,
    outline = SonggonOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = SonggonGreenLight,
    onPrimary = SonggonOnGreenContainer,
    primaryContainer = SonggonGreenDark,
    onPrimaryContainer = SonggonGreenContainer,
    secondary = SonggonAmber,
    onSecondary = SonggonOnAmberContainer,
    secondaryContainer = SonggonOnAmberContainer,
    onSecondaryContainer = SonggonAmberContainer,
    background = SonggonSlateDark,
    onBackground = SonggonBackground,
    surface = Color(0xFF1E293B),
    onSurface = SonggonBackground,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
)

@Composable
fun OjekSonggonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
