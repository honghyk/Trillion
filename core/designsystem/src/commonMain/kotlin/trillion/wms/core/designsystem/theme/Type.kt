package trillion.wms.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import trillionwms.core.designsystem.generated.resources.Pretendard_Black
import trillionwms.core.designsystem.generated.resources.Pretendard_Bold
import trillionwms.core.designsystem.generated.resources.Pretendard_ExtraBold
import trillionwms.core.designsystem.generated.resources.Pretendard_ExtraLight
import trillionwms.core.designsystem.generated.resources.Pretendard_Light
import trillionwms.core.designsystem.generated.resources.Pretendard_Medium
import trillionwms.core.designsystem.generated.resources.Pretendard_Regular
import trillionwms.core.designsystem.generated.resources.Pretendard_SemiBold
import trillionwms.core.designsystem.generated.resources.Pretendard_Thin
import trillionwms.core.designsystem.generated.resources.Res

@Composable
fun pretendardFontFamily() = FontFamily(
    Font(Res.font.Pretendard_Regular, FontWeight.Normal),
    Font(Res.font.Pretendard_Medium, FontWeight.Medium),
    Font(Res.font.Pretendard_SemiBold, FontWeight.SemiBold),
    Font(Res.font.Pretendard_Bold, FontWeight.Bold),
    Font(Res.font.Pretendard_ExtraBold, FontWeight.ExtraBold),
    Font(Res.font.Pretendard_Black, FontWeight.Black),
    Font(Res.font.Pretendard_ExtraLight, FontWeight.ExtraLight),
    Font(Res.font.Pretendard_Light, FontWeight.Light),
    Font(Res.font.Pretendard_Thin, FontWeight.Thin),
)

/**
 * Typography from [Figma Simple Design System](https://github.com/figma/sds/blob/main/src/theme.css)
 */
@Immutable
class SdsTypography(
    val bodyStrong: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    val bodyBase: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val bodyMedium: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    val bodySmall: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    val titleLarge: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 72.sp
    ),
    val titleMedium: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp
    ),
    val titleSmall: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp
    ),
    val headlineLarge: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp
    ),
    val headlineMedium: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    val headlineSmall: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    val labelSmall: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
) {

    fun copy(
        bodyStrong: TextStyle = this.bodyStrong,
        bodyBase: TextStyle = this.bodyBase,
        bodyMedium: TextStyle = this.bodyMedium,
        bodySmall: TextStyle = this.bodySmall,
        titleLarge: TextStyle = this.titleLarge,
        titleMedium: TextStyle = this.titleMedium,
        titleSmall: TextStyle = this.titleSmall,
        headlineLarge: TextStyle = this.headlineLarge,
        headlineMedium: TextStyle = this.headlineMedium,
        headlineSmall: TextStyle = this.headlineSmall,
        labelSmall: TextStyle = this.labelSmall
    ) = SdsTypography(
        bodyStrong = bodyStrong,
        bodyBase = bodyBase,
        bodyMedium = bodyMedium,
        bodySmall = bodySmall,
        titleLarge = titleLarge,
        titleMedium = titleMedium,
        titleSmall = titleSmall,
        headlineLarge = headlineLarge,
        headlineMedium = headlineMedium,
        headlineSmall = headlineSmall,
        labelSmall = labelSmall
    )
}
