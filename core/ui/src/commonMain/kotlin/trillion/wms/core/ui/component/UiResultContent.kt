package trillion.wms.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import trillion.wms.core.designsystem.component.ButtonSize
import trillion.wms.core.designsystem.component.ButtonVariant
import trillion.wms.core.designsystem.component.Icons
import trillion.wms.core.designsystem.component.SdsLoadingButton
import trillion.wms.core.designsystem.component.SdsSurface
import trillion.wms.core.designsystem.theme.SdsTheme
import trillion.wms.core.ui.utils.UiResult

@Composable
fun <T> UiResultContent(
    uiResult: UiResult<T>,
    isRefreshing: Boolean = false,
    onRetry: (() -> Unit)? = null,
    loading: @Composable () -> Unit = {
        LoadingContent()
    },
    error: @Composable () -> Unit = {
        ErrorContent(isRetrying = isRefreshing, onRetryClick = onRetry)
    },
    content: @Composable (T) -> Unit,
) {
    when (uiResult) {
        is UiResult.Loading -> loading()
        is UiResult.Error -> error()
        is UiResult.Success -> content(uiResult.data)
    }
}

@Composable
fun LoadingContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(40.dp),
            strokeWidth = 6.dp,
            color = SdsTheme.colorScheme.onBackgroundDefaultDefault,
        )
    }
}

@Composable
fun ErrorContent(
    modifier: Modifier = Modifier,
    isRetrying: Boolean = false,
    onRetryClick: (() -> Unit)? = null,
    errorMessage: String = "오류가 발생 했습니다",
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 32.dp, end = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = vectorResource(Icons.AlertCircle),
            tint = SdsTheme.colorScheme.iconDefaultSecondary,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
        )
        Spacer(modifier = Modifier.Companion.height(8.dp))
        Text(
            text = errorMessage,
            style = SdsTheme.typography.bodyStrong,
            textAlign = TextAlign.Companion.Center,
            modifier = Modifier.Companion.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (onRetryClick != null) {
            SdsLoadingButton(
                buttonVariant = ButtonVariant.Primary,
                buttonSize = ButtonSize.Medium,
                isLoading = isRetrying,
                text = "재시도",
                onClick = onRetryClick,
            )
        }
    }
}

@Composable
@Preview
private fun UiResultLoadingContentPreview() {
    SdsTheme {
        SdsSurface {
            UiResultContent(
                uiResult = UiResult.Loading,
                isRefreshing = false,
            ) {

            }
        }
    }
}

@Composable
@Preview
private fun UiResultErrorContentPreview() {
    SdsTheme {
        SdsSurface {
            UiResultContent(
                uiResult = UiResult.Error(RuntimeException()),
                isRefreshing = true,
                onRetry = { },
            ) {

            }
        }
    }
}
