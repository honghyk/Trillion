package trillion.wms.core.ui.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Instant

private val basicDateFormat = LocalDate.Formats.ISO_BASIC

object InstantFormatter {

    private val dottedDateFormat = LocalDate.Format {
        year()
        chars(". ")
        monthNumber()
        chars(". ")
        day()
    }

    private val abbreviatedMonthDayFormat = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        day()
    }

    private val fullDateFormat = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_FULL)
        chars(", ")
        monthName(MonthNames.ENGLISH_FULL)
        chars(" ")
        day()
        chars(", ")
        year()
    }

    private val fullDateTimeFormat = LocalDateTime.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_FULL)
        chars(", ")
        monthName(MonthNames.ENGLISH_FULL)
        chars(" ")
        day()
        chars(", ")
        year()
        chars(" at ")
        amPmHour()
        chars(":")
        minute()
        chars(" ")
        amPmMarker(am = "AM", pm = "PM")
    }

    fun formatDottedDate(
        instant: Instant,
        zone: TimeZone = TimeZone.currentSystemDefault(),
    ): String {
        return dottedDateFormat.format(instant.toLocalDateTime(zone).date)
    }

    fun formatBasicDate(
        instant: Instant,
        zone: TimeZone = TimeZone.currentSystemDefault(),
    ): String {
        return basicDateFormat.format(instant.toLocalDateTime(zone).date)
    }

    fun formatAbbreviatedMonthDay(
        instant: Instant,
        zone: TimeZone = TimeZone.currentSystemDefault(),
    ): String {
        return instant.toLocalDateTime(zone).date.format(abbreviatedMonthDayFormat)
    }

    fun formatRelative(
        instant: Instant,
        zone: TimeZone = TimeZone.currentSystemDefault(),
    ): String {
        val today = Clock.System.todayIn(zone)
        val date = instant.toLocalDateTime(zone).date

        return when {
            date == today -> "Today"
            date == today.minus(1, DateTimeUnit.DAY) -> "Yesterday"
            date == today.plus(1, DateTimeUnit.DAY) -> "Tomorrow"
            date.year == today.year -> date.format(abbreviatedMonthDayFormat)
            else -> date.format(dottedDateFormat)
        }
    }

    fun formatFullDate(
        instant: Instant,
        zone: TimeZone = TimeZone.currentSystemDefault(),
    ): String {
        return instant.toLocalDateTime(zone).date.format(fullDateFormat)
    }

    fun formatFullDateTime(
        instant: Instant,
        zone: TimeZone = TimeZone.currentSystemDefault(),
    ): String {
        return instant.toLocalDateTime(zone).format(fullDateTimeFormat)
    }
}
