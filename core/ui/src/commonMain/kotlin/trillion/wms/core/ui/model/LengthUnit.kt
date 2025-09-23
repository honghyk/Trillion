package trillion.wms.core.ui.model

enum class LengthUnit(val multiplier: Float, val unitName: String, val abbreviateName: String) {
    METER(1.0f, "미터", "m"),
    YARD(1.094f, "야드", "yd"),
}
