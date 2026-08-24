package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = GoldPrimary,
    onPrimary = EmeraldDark,
    primaryContainer = EmeraldMedium,
    onPrimaryContainer = Color.White,
    secondary = EmeraldLight,
    onSecondary = Color.White,
    secondaryContainer = EmeraldContainerDark,
    onSecondaryContainer = GoldLight,
    tertiary = GoldLight,
    onTertiary = EmeraldDark,
    tertiaryContainer = GoldContainerDark,
    onTertiaryContainer = GoldLight,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = EmeraldContainerLight,
    onPrimaryContainer = EmeraldDark,
    secondary = EmeraldMedium,
    onSecondary = Color.White,
    secondaryContainer = EmeraldContainerLight,
    onSecondaryContainer = EmeraldPrimary,
    tertiary = GoldDark,
    onTertiary = Color.White,
    tertiaryContainer = GoldContainerLight,
    onTertiaryContainer = GoldDark,
    background = CreamBackground,
    onBackground = WarmTextPrimary,
    surface = CreamSurface,
    onSurface = WarmTextPrimary,
    surfaceVariant = CreamSurfaceVariant,
    onSurfaceVariant = WarmTextSecondary,
    outline = WarmBorder,
  )

@Composable
fun AdhkariTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

