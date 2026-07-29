package com.pft.tracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Light Blue Palette
val BluePrimary = Color(0xFF4A90E2)
val BlueSecondary = Color(0xFF7CB9E8)
val BlueTertiary = Color(0xFFB0CFE0)
val BlueBackground = Color(0xFFF0F7FF) // Very light blue
val BlueGradientStart = Color(0xFFE6F2FF)
val BlueGradientEnd = Color(0xFFFFFFFF)

val BlobColor1 = Color(0xFFD0E6FF).copy(alpha = 0.4f)
val BlobColor2 = Color(0xFFEBF5FF).copy(alpha = 0.4f)

val PftRed = Color(0xFFD32F2F)
val PftAmber = Color(0xFFFBC02D)

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD0E6FF),
    onPrimaryContainer = Color(0xFF001E2F),
    secondary = BlueSecondary,
    onSecondary = Color.White,
    background = BlueBackground,
    surface = BlueBackground,
    error = PftRed
)

private val DarkColors = darkColorScheme(
    primary = BluePrimary, // Keep blue accent consistent
    onPrimary = Color.White,
    primaryContainer = Color(0xFF003354),
    onPrimaryContainer = Color(0xFFD0E6FF),
    secondary = BlueSecondary,
    onSecondary = Color.Black,
    background = Color.Black, // Pure black for AMOLED
    surface = Color(0xFF121212), // Dark grey for surfaces
    error = Color(0xFFF2B8B5)
)

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun PftTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        shapes = AppShapes,
        content = content
    )
}

val BudgetOverColor = PftRed
