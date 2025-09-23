@file:OptIn(ExperimentalMaterial3Api::class)

package trillion.wms.core.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun DropdownTextField(
    selected: String,
    onSelected: (index: Int) -> Unit,
    options: List<String>,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    contentPadding: PaddingValues = SdsTextFieldDefaults.contentPadding,
) {
    var expanded by remember { mutableStateOf(false) }
    val animatedRotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        val defaultColors = SdsTextFieldDefaults.colors()
        SdsTextField(
            value = selected,
            onValueChange = { },
            label = if (label != null) {
                { Text(text = label) }
            } else null,
            placeholder = if (placeholder != null) {
                { Text(text = placeholder) }
            } else null,
            trailingIcon = {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer { rotationZ = animatedRotation },
                    imageVector = vectorResource(Icons.ChevronDown),
                    contentDescription = "Expand",
                )
            },
            contentPadding = contentPadding,
            readOnly = true,
            enabled = false,
            isError = isError,
            colors = defaultColors.copy(
                disabledTextColor = defaultColors.textColor,
                disabledContainerColor = defaultColors.containerColor,
                disabledPlaceholderColor = defaultColors.placeholderColor,
                disabledLabelColor = defaultColors.labelColor,
                disabledTrailingIconColor = defaultColors.trailingIconColor,
                disabledBorderColor = defaultColors.borderColor,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        SdsExposedDropDownMenu(
            modifier = Modifier.heightIn(max = 240.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEachIndexed { index, option ->
                SdsDropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = SdsTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        onSelected(index)
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun DropdownIcon(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    menuItems: @Composable ColumnScope.() -> Unit,
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        SdsIconButton(
            onClick = { onExpandedChange(true) },
            modifier = Modifier
                .size(32.dp)
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            content = icon,
        )
        SdsExposedDropDownMenu(
            expanded = expanded,
            matchTextFieldWidth = false,
            onDismissRequest = { onExpandedChange(false) },
            content = menuItems,
        )
    }
}


@Composable
fun ExposedDropdownMenuBoxScope.SdsExposedDropDownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    matchTextFieldWidth: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    containerColor: Color = SdsTheme.colorScheme.backgroundDefaultDefault,
    shadowElevation: Dp = MenuDefaults.ShadowElevation,
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    ExposedDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        scrollState = scrollState,
        matchTextFieldWidth = matchTextFieldWidth,
        shape = shape,
        containerColor = containerColor,
        shadowElevation = shadowElevation,
        tonalElevation = MenuDefaults.TonalElevation,
        border = border,
        content = content
    )
}

@Composable
fun SdsDropdownMenuItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = RoundedCornerShape(4.dp),
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    Row(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clickable(
                enabled = enabled,
                onClick = onClick,
                interactionSource = interactionSource,
                indication = ripple(),
            )
            .clip(shape)
            .padding(
                top = 6.dp, bottom = 6.dp,
                start = 8.dp, end = 32.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProvideTextStyle(
            SdsTheme.typography.bodySmall,
            content = text
        )
    }
}
