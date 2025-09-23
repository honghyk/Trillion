package trillion.wms.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import trillion.wms.core.designsystem.extensions.hideKeyboardOnClick
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun FormDialog(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    action: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    SdsSurface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column {
                title()
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                        .hideKeyboardOnClick(),
                    content = content
                )
                action()
            }
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                snackbarHost()
            }
        }
    }
}

@Composable
fun FormDialogTitle(
    modifier: Modifier = Modifier,
    title: String = "",
    onDismiss: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.padding(bottom = FormTitleBottomPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = SdsTheme.typography.bodyStrong,
        )
        if (onDismiss != null) {
            SdsIconButton(
                modifier = Modifier.size(32.dp),
                onClick = onDismiss
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = vectorResource(Icons.Close),
                    contentDescription = "Close",
                )
            }
        }
    }
}

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    supportingText: @Composable (() -> Unit)? = null,
) {
    SdsTextField(
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        enabled = enabled,
        label = {
            Text(text = label)
        },
        placeholder = {
            Text(text = placeholder)
        },
        singleLine = singleLine,
        supportingText = supportingText,
        modifier = modifier.fillMaxWidth().padding(top = FormTextFieldTopPadding),
    )
}

@Composable
fun FormNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    supportingText: @Composable (() -> Unit)? = null,
) {
    SdsTextField(
        value = value,
        onValueChange = {
            if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                onValueChange(it)
            }
        },
        isError = isError,
        enabled = enabled,
        label = {
            Text(text = label)
        },
        placeholder = {
            Text(text = placeholder)
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
        singleLine = singleLine,
        supportingText = supportingText,
        modifier = modifier.fillMaxWidth().padding(top = FormTextFieldTopPadding),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDropDownField(
    value: String,
    onOptionSelected: (index: Int) -> Unit,
    label: String,
    options: List<String>,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
) {
    DropdownTextField(
        selected = value,
        onSelected = onOptionSelected,
        options = options,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        isError = isError
    )
}

@Composable
fun FormDateField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    isError: Boolean = false,
    singleLine: Boolean = true,
    supportingText: @Composable (() -> Unit)? = null,
) {
    SdsTextField(
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        enabled = enabled,
        visualTransformation = DateVisualTransformation(),
        label = {
            Text(text = label)
        },
        placeholder = {
            Text(text = placeholder)
        },
        singleLine = singleLine,
        supportingText = supportingText,
        modifier = modifier.fillMaxWidth().padding(top = FormTextFieldTopPadding),
    )
}

@Composable
fun FormVerticalTwoButton(
    primaryButtonState: FormButtonState,
    secondaryButtonState: FormButtonState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(top = FormButtonTopPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FormPrimaryButton(
            text = primaryButtonState.text,
            isLoading = primaryButtonState.loading,
            enabled = primaryButtonState.enabled,
            onClick = primaryButtonState.onClick,
            modifier = Modifier.fillMaxWidth(),
        )
        FormSecondaryButton(
            text = secondaryButtonState.text,
            onClick = secondaryButtonState.onClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun FormHorizontalTwoButton(
    primaryButtonState: FormButtonState,
    secondaryButtonState: FormButtonState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(top = FormButtonTopPadding),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FormSecondaryButton(
            text = secondaryButtonState.text,
            onClick = secondaryButtonState.onClick,
            modifier = Modifier.weight(1f),
        )
        FormPrimaryButton(
            text = primaryButtonState.text,
            isLoading = primaryButtonState.loading,
            enabled = primaryButtonState.enabled,
            onClick = primaryButtonState.onClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun FormPrimaryButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    SdsLoadingButton(
        buttonVariant = ButtonVariant.Primary,
        buttonSize = ButtonSize.Medium,
        enabled = enabled,
        isLoading = isLoading,
        text = text,
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
private fun FormSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SdsButton(
        buttonVariant = ButtonVariant.Subtle,
        buttonSize = ButtonSize.Medium,
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(text = text)
    }
}

class FormButtonState(
    val text: String,
    val enabled: Boolean = true,
    val loading: Boolean = false,
    val onClick: () -> Unit,
)

private class DateVisualTransformation : VisualTransformation {
    // Pattern: yyyy-MM-dd
    private val firstDelimiterOffset = 4
    private val secondDelimiterOffset = 7
    private val dateWithoutDelimiterLength = 8

    private val dateOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return when {
                offset < firstDelimiterOffset -> offset
                offset < secondDelimiterOffset - 1 -> offset + 1
                offset <= dateWithoutDelimiterLength -> offset + 2
                else -> dateWithoutDelimiterLength + 2
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return when {
                offset <= firstDelimiterOffset - 1 -> offset
                offset <= secondDelimiterOffset - 1 -> offset - 1
                offset <= dateWithoutDelimiterLength + 1 -> offset - 2
                else -> dateWithoutDelimiterLength
            }
        }
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmedText = if (text.length > dateWithoutDelimiterLength) {
            text.text.take(dateWithoutDelimiterLength)
        } else {
            text.text
        }
        var transformedText = ""
        for (i in trimmedText.indices) {
            transformedText += trimmedText[i]
            if (i + 1 == firstDelimiterOffset || i + 2 == secondDelimiterOffset) {
                transformedText += "-"
            }
        }
        return TransformedText(AnnotatedString(transformedText), dateOffsetTranslator)
    }
}


private val FormTitleBottomPadding = 8.dp

private val FormTextFieldTopPadding = 16.dp

private val FormButtonTopPadding = 16.dp
