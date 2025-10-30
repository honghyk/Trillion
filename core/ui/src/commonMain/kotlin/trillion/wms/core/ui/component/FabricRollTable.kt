package trillion.wms.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import trillion.wms.core.ui.model.LengthUnit
import trillion.wms.core.ui.utils.InstantFormatter
import trillion.wms.core.ui.utils.formatDecimal
import trillion.wms.core.designsystem.component.ColumnWidth
import trillion.wms.core.designsystem.component.ScrollableTable
import trillion.wms.core.model.FabricRoll

@Composable
fun FabricRollTable(
    fabricRolls: List<FabricRoll>,
    lengthUnit: LengthUnit,
    onRowClick: (FabricRoll) -> Unit,
    modifier: Modifier = Modifier,
    actionsCell: @Composable RowScope.(FabricRoll) -> Unit,
) {
    ScrollableTable(
        modifier = modifier,
        columnWidths = List(10) { ColumnWidth.MaxIntrinsicWidth },
        rows = fabricRolls,
        onRowClick = onRowClick,
        headerRowContent = { column ->
            when (column) {
                0 -> HeaderCell(text = "No")
                1 -> HeaderCell(text = "Item No")
                2 -> HeaderCell(text = "Order No")
                3 -> HeaderCell(text = "Color")
                4 -> HeaderCell(text = "Factory")
                5 -> HeaderCell(text = "Finish")
                6 -> HeaderCell(text = "Qty (${lengthUnit.unitName})")
                7 -> HeaderCell(text = "입고일")
                8 -> HeaderCell(text = "")
                9 -> HeaderCell(text = "Remark")
            }
        },
        dataRowContent = { fabricRoll, column, _ ->
            when (column) {
                0 -> TextCell(text = fabricRoll.id.toString())
                1 -> TextCell(text = fabricRoll.itemNo)
                2 -> TextCell(text = fabricRoll.orderNo.ifEmpty { "-" })
                3 -> TextCell(text = fabricRoll.color.ifEmpty { "-" })
                4 -> TextCell(text = fabricRoll.factory.ifEmpty { "-" })
                5 -> TextCell(text = fabricRoll.finish.ifEmpty { "-" })
                6 -> TextCell(
                    text = (fabricRoll.remainingQuantity * lengthUnit.multiplier).formatDecimal(1)
                )

                7 -> TextCell(text = InstantFormatter.formatDottedDate(fabricRoll.inboundAt))
                8 -> DataRowCell {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        actionsCell(this, fabricRoll)
                    }
                }

                9 -> TextCell(
                    modifier = Modifier.widthIn(max = 200.dp),
                    text = fabricRoll.remark.ifEmpty { "-" }
                )
            }
        }
    )
}
