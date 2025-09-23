package trillion.wms.core.designsystem.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

object SdsTheme {
    val colorScheme: SdsColorScheme
        @Composable
        get() = LocalSdsColorScheme.current

    val typography: SdsTypography
        @Composable
        get() = LocalSdsTypography.current

}

@Composable
fun SdsTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme()
    val typography = SdsTypography().let {
        val pretendardFontFamily = pretendardFontFamily()
        it.copy(
            bodyStrong = it.bodyStrong.copy(fontFamily = pretendardFontFamily),
            bodyBase = it.bodyBase.copy(fontFamily = pretendardFontFamily),
            bodyMedium = it.bodyMedium.copy(fontFamily = pretendardFontFamily),
            bodySmall = it.bodySmall.copy(fontFamily = pretendardFontFamily),
            titleLarge = it.titleLarge.copy(fontFamily = pretendardFontFamily),
            titleMedium = it.titleMedium.copy(fontFamily = pretendardFontFamily),
            titleSmall = it.titleSmall.copy(fontFamily = pretendardFontFamily),
            headlineLarge = it.headlineLarge.copy(fontFamily = pretendardFontFamily),
            headlineMedium = it.headlineMedium.copy(fontFamily = pretendardFontFamily),
            headlineSmall = it.headlineSmall.copy(fontFamily = pretendardFontFamily),
            labelSmall = it.labelSmall.copy(fontFamily = pretendardFontFamily),
        )
    }

    CompositionLocalProvider(
        LocalSdsColorScheme provides colorScheme,
        LocalSdsTypography provides typography,
        LocalContentColor provides colorScheme.textDefaultDefault,
        LocalTextStyle provides typography.bodyMedium,
        content = content,
    )
}

@Composable
fun ProvideLocalSdsColorScheme(colorScheme: SdsColorScheme, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalSdsColorScheme provides colorScheme, content = content)
}

@Composable
fun ProvideLocalSdsTypography(typography: SdsTypography, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalSdsTypography provides typography, content = content)
}

private val LocalSdsColorScheme = staticCompositionLocalOf<SdsColorScheme> {
    error("No SdsColorScheme provided")
}

private val LocalSdsTypography = staticCompositionLocalOf<SdsTypography> {
    error("No SdsTypography provided")
}
