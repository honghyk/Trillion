package trillion.wms.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import trillion.wms.core.designsystem.component.ButtonVariant
import trillion.wms.core.designsystem.component.Icons
import trillion.wms.core.designsystem.component.SdsButton
import trillion.wms.core.designsystem.component.SdsSurface
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun EmptyContent(
    icon: ImageVector,
    text: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    subText: String? = null,
    buttonLeadingIcon: ImageVector? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 32.dp, end = 32.dp, bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = SdsTheme.colorScheme.backgroundAccentDefault.copy(alpha = 0.4f),
                    shape = CircleShape,
                )
                .size(72.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "null",
                modifier = Modifier.size(32.dp),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = text,
            style = SdsTheme.typography.bodyStrong,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (subText != null) {
            Text(
                text = subText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        SdsButton(
            buttonVariant = ButtonVariant.Primary,
            leadingIcon = buttonLeadingIcon,
            onClick = onButtonClick,
            text = buttonText,
        )
    }
}

@Composable
@Preview
private fun EmptyContentPreview() {
    SdsTheme {
        SdsSurface {
            EmptyContent(
                icon = vectorResource(Icons.MapPin),
                text = "아직 생성된 구역이 없습니다",
                subText = "원단 롤 관리를 위해 보관 구역을 생성 해주세요",
                buttonText = "첫 구역 생성",
                onButtonClick = { },
            )
        }
    }
}
