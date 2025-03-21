package dev.mingaliev.wtf.designsystem

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

internal val LightColors = lightColors(
    primary = Palette.CaribbeanGreen,
    onPrimary = Color.Black,
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    background = Palette.Honeydew,
    onBackground = Color.Black,
    surface = Palette.Honeydew,
    onSurface = Color.Black
)

internal val DarkColors = darkColors(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF121212),
    onSurface = Color.White
)
