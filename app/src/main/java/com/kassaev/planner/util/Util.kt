package com.kassaev.planner.util

import com.kassaev.planner.R
import com.kassaev.planner.model.Month
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun isToday(date: String): Boolean =
    formatDateWithoutTime(SimpleDateFormat("yyyy-MM-dd").parse(date)) == formatDateWithoutTime(
        Calendar.getInstance().time
    )


fun getDay(date: String) = date.split("-").last().toInt().toString()

fun formatDateWithoutTime(date: Date) =
    SimpleDateFormat("yyyy-MM-dd", Locale("ru")).format(date)

fun getMonthResourceId(month: Month) =
    when (month.currentMonthDateList.first().split("-")[1].toInt()) {
        1 -> R.string.january
        2 -> R.string.february
        3 -> R.string.march
        4 -> R.string.april
        5 -> R.string.may
        6 -> R.string.june
        7 -> R.string.july
        8 -> R.string.august
        9 -> R.string.september
        10 -> R.string.october
        11 -> R.string.november
        12 -> R.string.december
        else -> R.string.unknown
    }

fun getYear(month: Month) =
    month.currentMonthDateList.first().split("-").first()

fun getCurrentDay(): String =
    Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()