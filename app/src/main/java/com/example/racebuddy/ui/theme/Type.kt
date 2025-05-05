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
val gabaritoExtraBoldTitleLargeTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.ExtraBold,  // ExtraBold weight
    fontSize = 55.sp,                  // Font size 55
    letterSpacing = 5.sp               // Letter spacing 9.sp
)

val gabaritoMediumBoldLargeBodyTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Medium,  // ExtraBold weight
    fontSize = 20.sp,                  // Font size 55
    //letterSpacing = 5.sp               // Letter spacing 9.sp
)

val gabaritoMediumBoldMediumBodyTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Medium,  // ExtraBold weight
    fontSize = 14.sp,                // Font size 55
    //letterSpacing = 5.sp               // Letter spacing 9.sp
)

val gabaritoSmallBodyTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp
)

// Default Material 3 typography values
val baseline = Typography()


// CHAT

val gabaritoDisplayLargeTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.ExtraBold,  // ExtraBold weight
    fontSize = 55.sp,                  // Font size 55
    letterSpacing = 5.sp
)

val gabaritoDisplayMediumTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 45.sp,
    letterSpacing = 0.sp
)

val gabaritoDisplaySmallTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 36.sp,
    letterSpacing = 0.sp
)

val gabaritoHeadlineLargeTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 32.sp,
    letterSpacing = 0.sp
)

val gabaritoHeadlineMediumTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 28.sp,
    letterSpacing = 0.sp
)

val gabaritoHeadlineSmallTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp,
    letterSpacing = 0.sp
)

val gabaritoTitleLargeTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 22.sp,
    letterSpacing = 0.sp
)

val gabaritoTitleMediumTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    //letterSpacing = 0.15.sp
)

val gabaritoTitleSmallTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    //letterSpacing = 0.1.sp
)

val gabaritoBodyLargeTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    //etterSpacing = 0.5.sp
)

val gabaritoBodyMediumTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    letterSpacing = 0.25.sp
)

val gabaritoBodySmallTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    letterSpacing = 0.4.sp
)

val gabaritoLabelLargeTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = 0.1.sp
)

val gabaritoLabelMediumTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    letterSpacing = 0.5.sp
)

val gabaritoLabelSmallTextStyle = TextStyle(
    fontFamily = gabaritoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    letterSpacing = 0.5.sp
)


//val AppTypography = Typography(
//    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
//    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
//    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
//    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
//    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
//    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
//    titleLarge = gabaritoExtraBoldTitleLargeTextStyle,
//    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
//    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
//    bodyLarge = gabaritoMediumBoldLargeBodyTextStyle,
//    bodyMedium = gabaritoMediumBoldMediumBodyTextStyle,
//    bodySmall = gabaritoSmallBodyTextStyle,
//    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
//    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
//    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
//)

val AppTypography = Typography(
    displayLarge = gabaritoDisplayLargeTextStyle,
    displayMedium = gabaritoDisplayMediumTextStyle,
    displaySmall = gabaritoDisplaySmallTextStyle,
    headlineLarge = gabaritoHeadlineLargeTextStyle,
    headlineMedium = gabaritoHeadlineMediumTextStyle,
    headlineSmall = gabaritoHeadlineSmallTextStyle,
    titleLarge = gabaritoTitleLargeTextStyle,
    titleMedium = gabaritoTitleMediumTextStyle,
    titleSmall = gabaritoTitleSmallTextStyle,
    bodyLarge = gabaritoBodyLargeTextStyle,
    bodyMedium = gabaritoBodyMediumTextStyle,
    bodySmall = gabaritoBodySmallTextStyle,
    labelLarge = gabaritoLabelLargeTextStyle,
    labelMedium = gabaritoLabelMediumTextStyle,
    labelSmall = gabaritoLabelSmallTextStyle
)




