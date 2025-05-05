package com.example.racebuddy.ui.theme
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.example.racebuddy.ui.theme.Typography
import okhttp3.internal.wait

private val baseNavyBlueColorScheme = lightColorScheme(
    primary = Color(0xFF4169E1),     // Navy Blue
   // primary = Color(0x6200EE),    // Purple 500 (Base color)
    onPrimary = Color.White,

    secondary = Color(0xFFFF6F61),            // Coral
    onSecondary = Color.White,

    tertiary = Color.Red,
    onTertiary = Color.White,

    surface = Color.White,
    onSurface = Color.Gray,

    background = Color(0xFFE6E6FA),           // Lavander
    onBackground = Color.Black
)

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

val lightColorSchemeChatGPT = lightColorScheme(
    primary = Color(0xFFFFDE21),              // bright sunny yellow
    onPrimary = Color.Black,                  // black text/icons on yellow
    primaryContainer = Color(0xFFFFF59D),     // soft yellow for surfaces
    onPrimaryContainer = Color(0xFF1C1C1C),   // dark gray for contrast
    secondary = Color(0xFF8D8D8D),            // neutral gray/taupe (used for surfaces/cards)
    onSecondary = Color.White,                // for text/icons on secondary
    background = Color.White,                 // dominant background
    onBackground = Color(0xFF1C1C1C),         // main text
    surface = Color(0xFFF8F8F8),              // subtle grayish-white surface
    onSurface = Color(0xFF1C1C1C),
    error = Color(0xFFB00020),
    onError = Color.White,
)

val lightColorSchemeNavyBlueChatGPT = lightColorScheme(
    primary = Color(0xFF001F54),              // Navy Blue as primary
    onPrimary = Color.White,                  // White text/icons on Navy Blue
    primaryContainer = Color(0xFF3A5B8B),     // Lighter shade of Navy for surfaces
    onPrimaryContainer = Color(0xFFFFFFFF),   // White text on primary container
    secondary = Color(0xFF8D8D8D),            // Neutral gray/taupe (used for surfaces/cards)
    onSecondary = Color.White,                // White text/icons on secondary
    background = Color.White,                 // Dominant white background
    onBackground = Color(0xFF1C1C1C),         // Main text in dark gray
    surface = Color(0xFFF8F8F8),              // Subtle grayish-white surface
    onSurface = Color(0xFF1C1C1C),            // Text on surface
    error = Color(0xFFB00020),                // Error color
    onError = Color.White,                    // Text/icons on error
)

val lightChatBothColorsScheme = lightColorScheme(
    primary = Color(0xFFFFDE21),            // Yellow
    onPrimary = Color(0xFFFFFFFF),          // White
    primaryContainer = Color(0xFFFFF59D),   // Warm Muted Gold

    secondary = Color(0xFF898989),          // Gray

    tertiary = Color(0xFFD3D3FF),           // Lavander
    onTertiary = Color(0xFF000080),         // Navy Blue (used as onTertiary for contrast, or vice versa)

    surface = Color(0x000000),            // White
    onSurface = Color(0xFFFFFBFE),          // Very light pink/white

    // Optional – set other fields if needed, or leave them default
    background = Color(0xFFFFFFFF),         // Typically matches surface
    onBackground = Color(0xFF000000),       // Black or dark gray (can adjust if needed)
    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF)
)

private val lightNavyBlueColorScheme = lightColorScheme(
    primary = Color(0x000080),                      // NavyBlue
    onPrimary = Color(0xFFFFFFFF),                  // White
    primaryContainer = Color(0xFF4B4B9F),           // softer/lighter version of navy
    onPrimaryContainer = Color(0xFFFFFFFF),         // White

    secondary = Color(0xFF4C6960),                  // Deep teal-gray
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFA8CEC1),         // Soft teal mint
    onSecondaryContainer = Color.Black,

    tertiary = Color(0xFF856D8A),                   // Desaturated purple-lavender
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE8D1EC),          // Soft pink-lavender
    onTertiaryContainer = Color.Black,

    surface = Color(0xFFF5F5F5),                    // Light neutral gray (Material default is often around this tone)
    onSurface = Color(0xFF1C1C1C),                  // Very dark gray for text/icon contrast

    background = Color(0xFFFFFFFF),                 // White
    onBackground = Color(0xFF000000),               // Black or dark gray for contrast

    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF)
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)


@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

private val simpleScheme = lightColorScheme(
    background = Color(0xF3F3F3)
)

private val material3Theme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

@Composable
fun RaceBuddyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable() () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = baseNavyBlueColorScheme,
        typography = AppTypography,
        content = content
    )
}

