package com.pft.tracker.domain.model

import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

data class MonthRange(val start: Long, val end: Long)

fun YearMonth.toEpochRange(zone: ZoneId = ZoneId.systemDefault()): MonthRange {
    val start = atDay(1).atStartOfDay(zone).toInstant().toEpochMilli()
    val end = atEndOfMonth().atTime(23, 59, 59, 999_000_000).atZone(zone).toInstant().toEpochMilli()
    return MonthRange(start, end)
}

fun LocalDate.toEpochRange(zone: ZoneId = ZoneId.systemDefault()): MonthRange {
    val start = atStartOfDay(zone).toInstant().toEpochMilli()
    val end = atTime(23, 59, 59, 999_000_000).atZone(zone).toInstant().toEpochMilli()
    return MonthRange(start, end)
}

fun customRange(from: LocalDate, to: LocalDate, zone: ZoneId = ZoneId.systemDefault()): MonthRange {
    val start = from.atStartOfDay(zone).toInstant().toEpochMilli()
    val end = to.atTime(23, 59, 59, 999_000_000).atZone(zone).toInstant().toEpochMilli()
    return MonthRange(start, end)
}
