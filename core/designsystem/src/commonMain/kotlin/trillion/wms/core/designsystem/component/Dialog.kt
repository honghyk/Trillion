package trillion.wms.core.designsystem.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun SdsAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    text: String,
    confirmButtonText: String,
    dismissButtonText: String = "취소",
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = SdsTheme.typography.bodyStrong,
            )
        },
        text = {
            Text(
                text = text,
                style = SdsTheme.typography.bodyMedium,
            )
        },
        confirmButton = {
            SdsButton(
                buttonVariant = ButtonVariant.Primary,
                buttonSize = ButtonSize.Small,
                onClick = onConfirm,
            ) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            SdsOutlineButton(
                buttonVariant = ButtonVariant.Primary,
                buttonSize = ButtonSize.Small,
                onClick = onDismiss,
            ) {
                Text(text = dismissButtonText)
            }
        },
        shape = RoundedCornerShape(8.dp),
        containerColor = SdsTheme.colorScheme.backgroundDefaultDefault,
        titleContentColor = SdsTheme.colorScheme.onBackgroundDefaultDefault,
        textContentColor = SdsTheme.colorScheme.onBackgroundDefaultSecondary,
    )
}

@Composable
@Preview
private fun AlertDialogPreview() {
    SdsTheme {
        SdsAlertDialog(
            onDismiss = {},
            onConfirm = {},
            title = "원단 롤 삭제",
            text = """
                원단 롤 "SLK-002"을(를) 정말 삭제하시겠습니까?

                이렇게 하면 원단 롤과 모든 출고 내역이 영구적으로 삭제됩니다.
            """.trimIndent(),
            confirmButtonText = "삭제",
        )
    }
}
