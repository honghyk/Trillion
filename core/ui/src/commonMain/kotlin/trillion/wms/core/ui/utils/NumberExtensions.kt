package trillion.wms.core.ui.utils

import kotlin.math.pow

fun Double.formatDecimal(decimalPlace: Int, isGroupingUsed: Boolean = true): String {
    val integerPart = this.toInt()
    val decimalPart = this - integerPart

    val integerStr =
        if (isGroupingUsed) {
            integerPart.toString().reversed().chunked(3).joinToString(",").reversed()
        } else {
            integerPart.toString()
        }
    if (decimalPart <= 0) {
        return integerStr
    }
    val factor = 10.0.pow(decimalPlace)
    val decimalStr =
        (decimalPart * factor).toInt().toString().padStart(decimalPlace, '0')
    return "$integerStr.$decimalStr"
}

fun Int.formatDecimal(isGroupingUsed: Boolean = true): String {
    return this.toDouble().formatDecimal(0, isGroupingUsed)
}
