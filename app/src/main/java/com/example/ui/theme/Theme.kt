package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RoyalColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = ObsidianBlack,
    primaryContainer = GoldContainer,
    onPrimaryContainer = OnGoldContainer,
    secondary = GoldLight,
    onSecondary = ObsidianBlack,
    secondaryContainer = Color(0xFF2E2616),
    onSecondaryContainer = GoldLight,
    tertiary = GoldAccent,
    onTertiary = ObsidianBlack,
    background = ObsidianBlack,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = CardDark,
    onSurfaceVariant = TextSecondary,
    outline = CardBorderGold,
    outlineVariant = Color(0xFF33333D),
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun AlMalakiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RoyalColorScheme,
        typography = Typography,
        content = content
    )
}
