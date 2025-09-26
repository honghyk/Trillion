package trillion.wms.core.ui.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeFormat
import trillion.wms.core.ui.utils.formatDecimal

abstract class FormFieldState<T> {
    abstract val value: T
    abstract val validators: List<Validator<T>>
    abstract val serverErrorMessage: String?

    val errorMessage: String?
        get() = serverErrorMessage ?: validators
            .map { it.validate(value) }
            .firstOrNull { it != null }

    val isError: Boolean get() = errorMessage != null
}

data class TextFormFieldState(
    override val value: String,
    override val validators: List<Validator<String>> = emptyList(),
    override val serverErrorMessage: String? = null,
) : FormFieldState<String>()

data class NumberFieldState(
    override val value: String,
    override val validators: List<Validator<String>> = listOf(Validators.isPositiveNumber()),
    override val serverErrorMessage: String? = null,
) : FormFieldState<String>()

data class DateFieldState(
    override val value: String,
    val format: DateTimeFormat<LocalDate> = LocalDate.Formats.ISO_BASIC,
    override val validators: List<Validator<String>> = listOf(Validators.isDate(format)),
    override val serverErrorMessage: String? = null,
) : FormFieldState<String>()

fun interface Validator<T> {
    fun validate(value: T): String?
}

object Validators {

    fun isPositiveNumber(): Validator<String> = Validator { value ->
        when {
            value.isEmpty() -> null
            value.toDoubleOrNull() == null -> "잘못된 숫자입니다"
            value.toDouble() <= 0 -> "잘못된 숫자입니다"
            else -> null
        }
    }

    fun isDate(
        format: DateTimeFormat<LocalDate> = LocalDate.Formats.ISO_BASIC,
    ): Validator<String> = Validator { value ->
        if (value.isEmpty()) return@Validator null
        try {
            format.parse(value)
            null
        } catch (_: IllegalArgumentException) {
            "잘못된 날짜 형식입니다"
        }
    }

    fun isInRange(
        max: Double,
        min: Double = 0.0,
        errorMessage: String? = null,
    ): Validator<String> = Validator { value ->
        when {
            value.isEmpty() -> null
            value.toDoubleOrNull() == null -> "잘못된 숫자입니다"
            value.toDouble() !in min..max -> errorMessage ?: "$min 이상 ${max.formatDecimal(1)} 이하여야 합니다"
            else -> null
        }
    }
}
