package kz.stierlitz.skillcinema.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

object SkillTheme {
    val typography: AppTypography
        @Composable
        get() = LocalTypography.current
}

@Composable
fun SkillCinemaTheme(content: @Composable () -> Unit) {
    val colorScheme = LightColorScheme

    val customTypography = AppTypography()


    CompositionLocalProvider(LocalTypography provides customTypography) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}