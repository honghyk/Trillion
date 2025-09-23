package trillion.wms.core.designsystem.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse

fun lightColorScheme() = SdsColorScheme(
    // Background
    backgroundDefaultDefault = Palette.white1000,
    onBackgroundDefaultDefault = Palette.gray900,
    backgroundDefaultSecondary = Palette.gray200,
    onBackgroundDefaultSecondary = Palette.gray500,
    backgroundDefaultSecondaryHover = Palette.gray300,
    backgroundDefaultTertiary = Palette.gray50,
    onBackgroundDefaultTertiary = Palette.gray400,
    backgroundDefaultTertiaryHover = Color(0xFFF7F7F7),

    backgroundBrandDefault = Palette.brand800,
    onBackgroundBrandDefault = Palette.brand100,
    backgroundBrandSecondary = Palette.brand200,
    onBackgroundBrandSecondary = Palette.brand900,
    backgroundBrandTertiary = Palette.brand100,
    onBackgroundBrandTertiary = Palette.brand800,

    backgroundAccentDefault = Palette.purple400,
    onBackgroundAccentDefault = Palette.gray900,

    backgroundNeutralDefault = Palette.slate700,
    onBackgroundNeutralDefault = Palette.slate100,
    backgroundNeutralSecondary = Palette.slate300,
    onBackgroundNeutralSecondary = Palette.slate900,
    backgroundNeutralTertiary = Palette.slate200,
    onBackgroundNeutralTertiary = Palette.slate800,

    backgroundDangerDefault = Palette.red500,
    onBackgroundDangerDefault = Palette.red100,
    backgroundDangerSecondary = Palette.red200,
    onBackgroundDangerSecondary = Palette.red700,
    backgroundDangerTertiary = Palette.red100,
    onBackgroundDangerTertiary = Palette.red700,

    backgroundPositiveDefault = Palette.green500,
    onBackgroundPositiveDefault = Palette.green100,
    backgroundPositiveSecondary = Palette.green200,
    onBackgroundPositiveSecondary = Palette.green800,
    backgroundPositiveTertiary = Palette.green100,
    onBackgroundPositiveTertiary = Palette.green800,

    backgroundDisabled = Palette.brand300,
    onBackgroundDisabled = Palette.brand400,

    // Border
    borderDefaultDefault = Palette.gray300,
    borderDefaultSecondary = Palette.gray500,
    borderDefaultTertiary = Palette.gray700,
    borderBrandDefault = Palette.brand800,
    borderBrandSecondary = Palette.brand600,
    borderBrandTertiary = Palette.brand500,
    borderNeutralDefault = Palette.slate900,
    borderNeutralSecondary = Palette.slate600,
    borderNeutralTertiary = Palette.slate400,
    borderDangerDefault = Palette.red700,
    borderDangerSecondary = Palette.red600,
    borderDangerTertiary = Palette.red500,
    borderPositiveDefault = Palette.green800,
    borderPositiveSecondary = Palette.green600,
    borderPositiveTertiary = Palette.green500,
    borderDisabled = Palette.brand400,

    // Text
    textDefaultDefault = Palette.gray900,
    textDefaultSecondary = Palette.gray500,
    textDefaultTertiary = Palette.gray400,

    textBrandDefault = Palette.brand800,
    textBrandSecondary = Palette.brand600,
    textBrandTertiary = Palette.brand500,

    textNeutralDefault = Palette.slate900,
    textDangerDefault = Palette.red700,
    textDangerSecondary = Palette.red600,
    textDangerTertiary = Palette.red500,
    textPositiveDefault = Palette.green800,
    textPositiveSecondary = Palette.green800,
    textPositiveTertiary = Palette.green800,
    textDisabled = Palette.brand400,

    // Icon
    iconDefaultDefault = Palette.gray900,
    iconDefaultSecondary = Palette.gray500,
    iconDefaultTertiary = Palette.gray400,
    iconBrandDefault = Palette.brand800,
    iconBrandSecondary = Palette.brand600,
    iconNeutralDefault = Palette.slate900,
    iconDangerDefault = Palette.red700,
    iconPositiveDefault = Palette.green800,
    iconPositiveTertiary = Palette.green600,
    iconDisabled = Palette.brand400,
)

@Immutable
class SdsColorScheme(
    // Background
    val backgroundDefaultDefault: Color,
    val onBackgroundDefaultDefault: Color,
    val backgroundDefaultSecondary: Color,
    val onBackgroundDefaultSecondary: Color,
    val backgroundDefaultSecondaryHover: Color,
    val backgroundDefaultTertiary: Color,
    val onBackgroundDefaultTertiary: Color,
    val backgroundDefaultTertiaryHover: Color,

    val backgroundBrandDefault: Color,
    val onBackgroundBrandDefault: Color,
    val backgroundBrandSecondary: Color,
    val onBackgroundBrandSecondary: Color,
    val backgroundBrandTertiary: Color,
    val onBackgroundBrandTertiary: Color,

    val backgroundAccentDefault: Color,
    val onBackgroundAccentDefault: Color,

    val backgroundNeutralDefault: Color,
    val onBackgroundNeutralDefault: Color,
    val backgroundNeutralSecondary: Color,
    val onBackgroundNeutralSecondary: Color,
    val backgroundNeutralTertiary: Color,
    val onBackgroundNeutralTertiary: Color,

    val backgroundDangerDefault: Color,
    val onBackgroundDangerDefault: Color,
    val backgroundDangerSecondary: Color,
    val onBackgroundDangerSecondary: Color,
    val backgroundDangerTertiary: Color,
    val onBackgroundDangerTertiary: Color,

    val backgroundPositiveDefault: Color,
    val onBackgroundPositiveDefault: Color,
    val backgroundPositiveSecondary: Color,
    val onBackgroundPositiveSecondary: Color,
    val backgroundPositiveTertiary: Color,
    val onBackgroundPositiveTertiary: Color,

    val backgroundDisabled: Color,
    val onBackgroundDisabled: Color,

    // Border
    val borderDefaultDefault: Color,
    val borderDefaultSecondary: Color,
    val borderDefaultTertiary: Color,
    val borderBrandDefault: Color,
    val borderBrandSecondary: Color,
    val borderBrandTertiary: Color,
    val borderNeutralDefault: Color,
    val borderNeutralSecondary: Color,
    val borderNeutralTertiary: Color,
    val borderDangerDefault: Color,
    val borderDangerSecondary: Color,
    val borderDangerTertiary: Color,
    val borderPositiveDefault: Color,
    val borderPositiveSecondary: Color,
    val borderPositiveTertiary: Color,
    val borderDisabled: Color,

    // Text
    val textDefaultDefault: Color,
    val textDefaultSecondary: Color,
    val textDefaultTertiary: Color,

    val textBrandDefault: Color,
    val textBrandSecondary: Color,
    val textBrandTertiary: Color,

    val textNeutralDefault: Color,
    val textDangerDefault: Color,
    val textDangerSecondary: Color,
    val textDangerTertiary: Color,
    val textPositiveDefault: Color,
    val textPositiveSecondary: Color,
    val textPositiveTertiary: Color,
    val textDisabled: Color,

    // Icon
    val iconDefaultDefault: Color,
    val iconDefaultSecondary: Color,
    val iconDefaultTertiary: Color,
    val iconNeutralDefault: Color,
    val iconBrandDefault: Color,
    val iconBrandSecondary: Color,
    val iconDangerDefault: Color,
    val iconPositiveDefault: Color,
    val iconPositiveTertiary: Color,
    val iconDisabled: Color,
)

/**
 * Color palette from [Figma Simple Design System](https://github.com/figma/sds/blob/main/src/theme.css)
 */
private object Palette {
    // Black
    val black100 = Color(0x0C0C0D0D)
    val black200 = Color(0x0C0C0D1A)
    val black300 = Color(0x0C0C0D33)
    val black400 = Color(0x0C0C0D66)
    val black500 = Color(0x0C0C0DB2)
    val black600 = Color(0x0C0C0DCC)
    val black700 = Color(0x0C0C0DD9)
    val black800 = Color(0x0C0C0DE5)
    val black900 = Color(0x0C0C0DF2)
    val black1000 = Color(0xFF0C0C0D)

    // Brand
    val brand100 = Color(0xFFF5F5F5)
    val brand200 = Color(0xFFE6E6E6)
    val brand300 = Color(0xFFD9D9D9)
    val brand400 = Color(0xFFB3B3B3)
    val brand500 = Color(0xFF757575)
    val brand600 = Color(0xFF444444)
    val brand700 = Color(0xFF383838)
    val brand800 = Color(0xFF2C2C2C)
    val brand900 = Color(0xFF1E1E1E)
    val brand1000 = Color(0xFF111111)

    // Gray
    val gray50 = Color(0xFFFCFCFC)
    val gray100 = Color(0xFFF5F5F5)
    val gray200 = Color(0xFFE6E6E6)
    val gray300 = Color(0xFFD9D9D9)
    val gray400 = Color(0xFFB3B3B3)
    val gray500 = Color(0xFF757575)
    val gray600 = Color(0xFF444444)
    val gray700 = Color(0xFF383838)
    val gray800 = Color(0xFF2C2C2C)
    val gray900 = Color(0xFF1E1E1E)
    val gray1000 = Color(0xFF111111)

    // Green
    val green100 = Color(0xFFEBFFEE)
    val green200 = Color(0xFFCFF7D3)
    val green300 = Color(0xFFAFF4C6)
    val green400 = Color(0xFF85E0A3)
    val green500 = Color(0xFF14AE5C)
    val green600 = Color(0xFF009951)
    val green700 = Color(0xFF008043)
    val green800 = Color(0xFF02542D)
    val green900 = Color(0xFF024023)
    val green1000 = Color(0xFF062D1B)

    // pink
    val pink100 = Color(0xFFFCF1FD)
    val pink200 = Color(0xFFFAE1FA)
    val pink300 = Color(0xFFF5C0EF)
    val pink400 = Color(0xFFF19EDC)
    val pink500 = Color(0xFFEA3FB8)
    val pink600 = Color(0xFFD732A8)
    val pink700 = Color(0xFFBA2A92)
    val pink800 = Color(0xFF8A226F)
    val pink900 = Color(0xFF57184A)
    val pink1000 = Color(0xFF3F1536)

    // red
    val red100 = Color(0xFFFEE9E7)
    val red200 = Color(0xFFFDD3D0)
    val red300 = Color(0xFFFCB3AD)
    val red400 = Color(0xFFF4776A)
    val red500 = Color(0xFFEC221F)
    val red600 = Color(0xFFC00F0C)
    val red700 = Color(0xFF900B09)
    val red800 = Color(0xFF690807)
    val red900 = Color(0xFF4D0B0A)
    val red1000 = Color(0xFF300603)

    // slate
    val slate100 = Color(0xFFF3F3F3)
    val slate200 = Color(0xFFE3E3E3)
    val slate300 = Color(0xFFCDCDCD)
    val slate400 = Color(0xFFB2B2B2)
    val slate500 = Color(0xFF949494)
    val slate600 = Color(0xFF767676)
    val slate700 = Color(0xFF5A5A5A)
    val slate800 = Color(0xFF434343)
    val slate900 = Color(0xFF303030)
    val slate1000 = Color(0xFF242424)

    // white
    val white100 = Color(0x1AFFFFFF)
    val white200 = Color(0x33FFFFFF)
    val white300 = Color(0x4DFFFFFF)
    val white400 = Color(0x66FFFFFF)
    val white500 = Color(0xB2FFFFFF)
    val white600 = Color(0xCCFFFFFF)
    val white700 = Color(0xD9FFFFFF)
    val white800 = Color(0xE5FFFFFF)
    val white900 = Color(0xF2FFFFFF)
    val white1000 = Color(0xFFFFFFFF)

    // yellow
    val yellow100 = Color(0xFFFFFBEB)
    val yellow200 = Color(0xFFFFF1C2)
    val yellow300 = Color(0xFFFFE8A3)
    val yellow400 = Color(0xFFE8B931)
    val yellow500 = Color(0xFFE5A000)
    val yellow600 = Color(0xFFBF6A02)
    val yellow700 = Color(0xFF975102)
    val yellow800 = Color(0xFF682D03)
    val yellow900 = Color(0xFF522504)
    val yellow1000 = Color(0xFF401B01)

    // purple
    val purple100 = Color(0xFFFEFBFF)
    val purple400 = Color(0xFFEADDFF)
}

@Stable
fun SdsColorScheme.contentColorFor(backgroundColor: Color): Color =
    when (backgroundColor) {
        backgroundDefaultDefault -> onBackgroundDefaultDefault
        backgroundDefaultSecondary -> onBackgroundDefaultSecondary
        backgroundDefaultTertiary -> onBackgroundDefaultTertiary

        backgroundBrandDefault -> onBackgroundBrandDefault
        backgroundBrandSecondary -> onBackgroundBrandSecondary
        backgroundBrandTertiary -> onBackgroundBrandTertiary

        backgroundNeutralDefault -> onBackgroundNeutralDefault
        backgroundNeutralSecondary -> onBackgroundNeutralSecondary
        backgroundNeutralTertiary -> onBackgroundNeutralTertiary

        backgroundAccentDefault -> onBackgroundAccentDefault

        backgroundDangerDefault -> backgroundDangerDefault
        backgroundDangerSecondary -> onBackgroundDangerSecondary
        backgroundDangerTertiary -> onBackgroundDangerTertiary

        backgroundPositiveDefault -> backgroundPositiveDefault
        backgroundPositiveSecondary -> onBackgroundPositiveSecondary
        backgroundPositiveTertiary -> onBackgroundPositiveTertiary

        backgroundDisabled -> onBackgroundDisabled
        else -> Color.Unspecified
    }

@Composable
fun contentColorFor(backgroundColor: Color): Color {
    return SdsTheme.colorScheme.contentColorFor(backgroundColor).takeOrElse {
        LocalContentColor.current
    }
}
