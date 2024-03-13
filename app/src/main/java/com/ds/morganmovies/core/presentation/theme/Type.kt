package com.ds.morganmovies.core.presentation.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ds.morganmovies.R

// Set of Material typography styles to start with

val ComicSansFontFamily = FontFamily(
    Font(R.font.comic_sans, FontWeight.Normal),
    Font(R.font.comic_sans_bold, FontWeight.Bold)
)

val SephirFontFamily = FontFamily(
    Font(R.font.sephir_regular, FontWeight.Normal)
)

val MulheimFontFamily = FontFamily(
    Font(R.font.mulheim, FontWeight.Normal)
)



val Typography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

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