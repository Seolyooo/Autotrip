package com.example.autotrip.ui.plan.components

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/*
 * ---------------------------------------------------------
 * 날짜 변환
 * ---------------------------------------------------------
 */

internal fun formatKoreanDateRange(
    startMillis: Long?,
    endMillis: Long?
): String {

    if (
        startMillis == null ||
        endMillis == null
    ) {
        return ""
    }

    val start =
        millisToLocalDate(startMillis)

    val end =
        millisToLocalDate(endMillis)

    return if (start.year == end.year) {

        "${start.year}년 ${start.monthValue}월 ${start.dayOfMonth}일 ~ " +
            "${end.monthValue}월 ${end.dayOfMonth}일"

    } else {

        "${start.year}년 ${start.monthValue}월 ${start.dayOfMonth}일 ~ " +
            "${end.year}년 ${end.monthValue}월 ${end.dayOfMonth}일"
    }
}

internal fun millisToLocalDate(
    millis: Long
): LocalDate {

    return Instant
        .ofEpochMilli(millis)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
}

internal fun localDateToMillis(
    date: LocalDate
): Long {

    return date
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()
}
