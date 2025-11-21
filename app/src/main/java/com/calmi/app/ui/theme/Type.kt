package com.calmi.app.ui.theme


import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.calmi.app.R


// Font Families
val Roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
)

val Jakarta = FontFamily(
    Font(R.font.jakarta_semibold, FontWeight.SemiBold),
)

// Typography System
val CalmiTypography = Typography(

    displayLarge = TextStyle(
        fontFamily = Jakarta,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = Jakarta,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),

    titleMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    bodySmall = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = CalmTextSecondary
    )
)