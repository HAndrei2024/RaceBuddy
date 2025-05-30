package com.example.racebuddy.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

val paddings = Spacing()
val heights = ElementHeights()
val sizes = Sizes()

data class Spacing(
    val spacingNone: Dp = 1.dp,
    val spacingXSmall: Dp = 4.dp,
    val spacingSmall : Dp = 10.dp,
    val spacingMedium: Dp = 16.dp,
    val spacingLarge: Dp = 24.dp,
    val spacingExtraLarge: Dp = 56.dp
)

data class ElementHeights(
    val xSmall: Dp = 30.dp,
    val small: Dp = 40.dp,
    val medium: Dp = 56.dp, // standard TextField/Button height
    val large: Dp = 72.dp,
    val largeish: Dp = 80.dp,
    val extraLarge: Dp = 88.dp
)

data class Sizes(
    val xSmall: Dp = 20.dp,
    val small: Dp = 40.dp,
    val medium: Dp = 56.dp, // standard TextField/Button height
    val large: Dp = 72.dp,
    val extraLarge: Dp = 88.dp
)

