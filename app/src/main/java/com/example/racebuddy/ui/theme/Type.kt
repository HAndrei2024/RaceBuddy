package com.example.racebuddy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.racebuddy.R
import androidx.compose.ui.text.font.Font

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

//val robotoSlab = GoogleFont("Roboto Slab")
//val openSans = GoogleFont("Open Sans")
//
//val AppTypography = Typography(
//    displayLarge = TextStyle(
//        fontFamily = FontFamily(robotoSlab),
//        fontWeight = FontWeight.Normal,
//        fontSize = 48.sp,
//        lineHeight = 56.sp,
//        letterSpacing = 0.sp
//    ),
//    headlineMedium = TextStyle(
//        fontFamily = FontFamily(robotoSlab),
//        fontWeight = FontWeight.Normal,
//        fontSize = 24.sp,
//        lineHeight = 32.sp,
//        letterSpacing = 0.sp
//    ),
//    titleLarge = TextStyle(
//        fontFamily = FontFamily(robotoSlab),
//        fontWeight = FontWeight.Normal,
//        fontSize = 20.sp,
//        lineHeight = 28.sp,
//        letterSpacing = 0.sp
//    ),
//    bodyMedium = TextStyle(
//        fontFamily = FontFamily(openSans),
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.15.sp
//    ),
//    labelLarge = TextStyle(
//        fontFamily = FontFamily(openSans),
//        fontWeight = FontWeight.Medium,
//        fontSize = 14.sp,
//        lineHeight = 20.sp,
//        letterSpacing = 0.1.sp
//    )
//)

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Open Sans"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto Slab"),
        fontProvider = provider,
        weight = FontWeight(400)
    )
)



val gabaritoFontFamily = FontFamily(
    Font(R.font.gabarito_variablefont_wght) // Replace with the correct font resource
)

// Define a reusable TextStyle for the custom font
val gabaritoExtraBoldTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.ExtraBold,  // ExtraBold weight
    fontSize = 55.sp,                  // Font size 55
    letterSpacing = 5.sp               // Letter spacing 9.sp
)

val gabaritoMediumBoldTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Medium,  // ExtraBold weight
    fontSize = 20.sp,                  // Font size 55
    //letterSpacing = 5.sp               // Letter spacing 9.sp
)

val gabaritoMediumBoldGrayTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Medium,  // ExtraBold weight
    fontSize = 14.sp,                // Font size 55
    color = Color.Gray
    //letterSpacing = 5.sp               // Letter spacing 9.sp
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
    titleLarge = gabaritoExtraBoldTextStyle,
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
    bodyLarge = gabaritoMediumBoldTextStyle,
    bodyMedium = gabaritoMediumBoldGrayTextStyle,
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
)



