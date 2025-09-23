package trillion.wms.core.designsystem.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import trillion.wms.core.designsystem.theme.SdsTheme

@Composable
fun <T> ScrollableTable(
    columnWidths: List<ColumnWidth.Scrollable>,
    rows: List<T>,
    modifier: Modifier = Modifier,
    horizontalScrollState: ScrollState = rememberScrollState(),
    onRowClick: (T) -> Unit = {},
    headerRowContent: @Composable TableRowScope.(column: Int) -> Unit,
    dataRowContent: @Composable TableRowScope.(item: T, column: Int, row: Int) -> Unit,
) {
    SdsCard(modifier = modifier) {
        Table(
            columnWidths = columnWidths,
            rows = rows,
            onRowClick = onRowClick,
            horizontalScrollState = horizontalScrollState,
            headerRowContent = headerRowContent,
            dataRowContent = dataRowContent,
        )
    }
}

@Composable
fun <T> FixedTable(
    columnWidths: List<ColumnWidth.Fixed>,
    rows: List<T>,
    modifier: Modifier = Modifier,
    onRowClick: (T) -> Unit = {},
    headerRowContent: @Composable TableRowScope.(column: Int) -> Unit,
    dataRowContent: @Composable TableRowScope.(item: T, column: Int, row: Int) -> Unit,
) {
    SdsCard(modifier = modifier) {
        Table(
            columnWidths = columnWidths,
            rows = rows,
            horizontalScrollState = null,
            onRowClick = onRowClick,
            headerRowContent = headerRowContent,
            dataRowContent = dataRowContent,
        )
    }
}


@Composable
private fun <T> Table(
    columnWidths: List<ColumnWidth>,
    rows: List<T>,
    onRowClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    horizontalScrollState: ScrollState?,
    headerRowContent: @Composable TableRowScope.(column: Int) -> Unit,
    dataRowContent: @Composable TableRowScope.(item: T, column: Int, row: Int) -> Unit,
) {
    val tableScope = remember(columnWidths) { TableScopeImpl() }
    val horizontalScrollableModifier = if (horizontalScrollState != null) {
        Modifier.horizontalScroll(horizontalScrollState)
    } else {
        Modifier
    }

    BoxWithConstraints(modifier = modifier) {
        val dividerWidth = maxWidth

        with(tableScope) {
            Column(modifier = modifier) {
                TableHeaderRow(
                    modifier = Modifier
                        .then(horizontalScrollableModifier),
                    columnWidths = columnWidths,
                    content = headerRowContent,
                )
                Divider(modifier = Modifier.width(dividerWidth))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(horizontalScrollableModifier),
                ) {
                    itemsIndexed(rows) { index, item ->
                        Column(
                            modifier = if (horizontalScrollState == null) {
                                Modifier
                            } else {
                                Modifier.width(IntrinsicSize.Max)
                            }
                        ) {
                            TableDataRow(
                                modifier = Modifier.widthIn(dividerWidth),
                                columnWidths = columnWidths,
                                onRowClick = { onRowClick(item) },
                                content = { column -> dataRowContent(item, column, index) },
                            )
                            if (index < rows.lastIndex) {
                                Divider(modifier = Modifier.widthIn(dividerWidth).fillMaxWidth())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TableScope.TableHeaderRow(
    columnWidths: List<ColumnWidth>,
    modifier: Modifier = Modifier,
    scrollState: ScrollState? = null,
    content: @Composable TableRowScope.(column: Int) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (scrollState == null) Modifier else Modifier.horizontalScroll(scrollState)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val tableRowScope = remember { TableRowScopeImpl(this@TableHeaderRow, this) }
        columnWidths.forEachIndexed { column, columnWidth ->
            tableRowScope.TableCell(
                columnWidth = columnWidth,
                column = column,
                content = { content(tableRowScope, column) }
            )
        }
    }
}

@Composable
private fun TableScope.TableDataRow(
    columnWidths: List<ColumnWidth>,
    onRowClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable TableRowScope.(column: Int) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onRowClick,
                interactionSource = null,
                indication = ripple()
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val tableRowScope = remember { TableRowScopeImpl(this@TableDataRow, this) }
        columnWidths.forEachIndexed { column, columnWidth ->
            tableRowScope.TableCell(
                columnWidth = columnWidth,
                column = column,
                content = { content(tableRowScope, column) }
            )
        }
    }
}

@Composable
private fun TableRowScope.TableCell(
    columnWidth: ColumnWidth,
    column: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .then(
                when (columnWidth) {
                    ColumnWidth.MaxIntrinsicWidth -> Modifier.columnMaxIntrinsicWidth(column)
                    is ColumnWidth.Size -> Modifier.requiredWidth(columnWidth.width)
                    is ColumnWidth.Weight -> Modifier.weight(columnWidth.weight)
                }
            ),
    ) { content() }
}

@Composable
private fun Divider(modifier: Modifier = Modifier) {
    SdsDivider(modifier = modifier)
}

sealed interface ColumnWidth {
    sealed interface Scrollable : ColumnWidth
    sealed interface Fixed : ColumnWidth

    data class Size(val width: Dp) : Fixed, Scrollable

    data object MaxIntrinsicWidth : Fixed, Scrollable

    data class Weight(val weight: Float = 1.0f) : Fixed
}

interface TableScope {
    fun Modifier.columnMaxIntrinsicWidth(column: Int): Modifier
}

interface TableRowScope : TableScope, RowScope {

    @Composable
    fun HeaderCell(
        text: String,
        modifier: Modifier = Modifier,
    )

    @Composable
    fun DataRowCell(
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit,
    )

    @Composable
    fun TextCell(
        text: String,
        modifier: Modifier = Modifier,
    )
}

private class TableScopeImpl : TableScope {
    val columnWidths = mutableStateMapOf<Int, Int>()

    override fun Modifier.columnMaxIntrinsicWidth(column: Int) = layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        val currentMaxWidth = columnWidths[column] ?: 0
        val maxWidth = maxOf(currentMaxWidth, placeable.width)

        if (currentMaxWidth != maxWidth) {
            columnWidths[column] = maxWidth
        }

        layout(width = maxWidth, height = placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}

private class TableRowScopeImpl(
    tableScope: TableScope,
    rowScope: RowScope
) : TableRowScope, TableScope by tableScope, RowScope by rowScope {

    @Composable
    override fun HeaderCell(
        text: String,
        modifier: Modifier,
    ) {
        Box(modifier = modifier.padding(CellContentPadding)) {
            Text(
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                text = text,
            )
        }
    }

    @Composable
    override fun DataRowCell(modifier: Modifier, content: @Composable () -> Unit) {
        Box(modifier = modifier.padding(CellContentPadding)) {
            content()
        }
    }

    @Composable
    override fun TextCell(text: String, modifier: Modifier) {
        DataRowCell(modifier = modifier) {
            Text(text = text)
        }
    }
}

private val CellContentHorizontalPadding: Dp = 16.dp
private val CellContentVerticalPadding: Dp = 12.dp

private val CellContentPadding: PaddingValues = PaddingValues(
    horizontal = CellContentHorizontalPadding,
    vertical = CellContentVerticalPadding,
)

@Composable
@Preview
private fun TablePreview() {
    SdsTheme {
        val items = generateFakeFabricRolls(100)
        ScrollableTable(
            columnWidths = List(4) { ColumnWidth.MaxIntrinsicWidth },
            rows = items,
            headerRowContent = { column ->
                when (column) {
                    0 -> HeaderCell(
                        modifier = Modifier,
                        text = "ID"
                    )

                    1 -> HeaderCell(
                        modifier = Modifier,
                        text = "ITEM NO"
                    )

                    2 -> HeaderCell(
                        modifier = Modifier,
                        text = "REMAINING QUANTITY"
                    )

                    3 -> HeaderCell(
                        modifier = Modifier,
                        text = "FACTORY"
                    )
                }
            },
            dataRowContent = { item, column, _ ->
                when (column) {
                    0 -> TextCell(
                        modifier = Modifier,
                        text = item.id.toString()
                    )

                    1 -> TextCell(
                        modifier = Modifier,
                        text = item.itemNo
                    )

                    2 -> TextCell(
                        modifier = Modifier,
                        text = item.remainingQuantity.toString()
                    )

                    3 -> TextCell(
                        modifier = Modifier,
                        text = item.factory
                    )
                }
            },
        )
    }
}

private data class FabricRoll(
    val id: Int,
    val itemNo: String,
    val remainingQuantity: Double,
    val factory: String
)

private fun generateFakeFabricRolls(count: Int) = List(count) {
    FabricRoll(
        it + 1,
        "ITEM-${it + 1}",
        (100..1000).random().toDouble(),
        "Factory ${(it % 5) + 1}"
    )
}
