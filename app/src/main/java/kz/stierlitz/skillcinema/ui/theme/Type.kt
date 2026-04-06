package kz.stierlitz.skillcinema.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kz.stierlitz.skillcinema.R

val graphiksFamily = FontFamily(
    Font(R.font.graphik_bold, FontWeight.Bold),
    Font(R.font.graphik_medium, FontWeight.Medium),
    Font(R.font.graphik_regular, FontWeight.Normal),
    Font(R.font.graphik_semibold, FontWeight.SemiBold),
)

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

@Immutable
data class AppTypography(
    val graphiksRegular: TextStyle = TextStyle(
        fontFamily = graphiksFamily,
        fontWeight = FontWeight.Normal,
    ).withDefaultFontFamily(graphiksFamily),

    val graphiksBold: TextStyle = TextStyle(
        fontFamily = graphiksFamily,
        fontWeight = FontWeight.Bold,
    ).withDefaultFontFamily(graphiksFamily),

    val graphiksSemiBold: TextStyle = TextStyle(
        fontFamily = graphiksFamily,
        fontWeight = FontWeight.SemiBold,
    ).withDefaultFontFamily(graphiksFamily),

    val graphiksMedium: TextStyle = TextStyle(
        fontFamily = graphiksFamily,
        fontWeight = FontWeight.Medium,
    ).withDefaultFontFamily(graphiksFamily)
)

private fun TextStyle.withDefaultFontFamily(default: FontFamily): TextStyle {
    return if (fontFamily != null) this else copy(fontFamily = default)
}

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }
