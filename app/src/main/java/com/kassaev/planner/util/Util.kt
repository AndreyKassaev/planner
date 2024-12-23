package com.kassaev.planner.util

import com.kassaev.planner.R
import com.kassaev.planner.model.Month
import com.kassaev.planner.model.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun isToday(date: String): Boolean =
    formatDateWithoutTime(SimpleDateFormat("yyyy-MM-dd").parse(date)) == formatDateWithoutTime(
        Calendar.getInstance().time
    )


fun getDay(date: String) = date.split("-").last().toInt().toString()

fun formatDateWithoutTime(date: Date) =
    SimpleDateFormat("yyyy-MM-dd", Locale("ru")).format(date)

fun formatDateWithDayAndMonth(date: Date) =
    SimpleDateFormat("dd MMMM", Locale.getDefault()).format(date)

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

fun getDayStartFinishTimestampPair(dateString: String): Pair<Long, Long>? {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    return try {
        val date = dateFormat.parse(dateString) ?: return null
        val calendar = Calendar.getInstance()
        calendar.time = date

        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = calendar.timeInMillis

        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        val endOfDay = calendar.timeInMillis

        Pair(startOfDay, endOfDay)
    } catch (e: Exception) {
        null
    }
}

fun formatTime(input: Int): String =
    String.format("%02d", input)


fun hourMinutePairToTimestamp(hourMinutePair: Pair<Int, Int>, currentDateTimestamp: Long): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentDateTimestamp
    calendar.set(Calendar.HOUR_OF_DAY, hourMinutePair.first)
    calendar.set(Calendar.MINUTE, hourMinutePair.second)

    return calendar.timeInMillis
}

fun timestampToDate(timestamp: Long): Date {
    val calendar = Calendar.getInstance()
    calendar.timeZone = TimeZone.getTimeZone(TimeZone.getDefault().id)
    calendar.timeInMillis = timestamp
    return calendar.time
}

fun getDayOfMonth(date: Date): Int {
    val calendar = Calendar.getInstance().apply {
        time = date
    }
    return calendar.get(Calendar.DAY_OF_MONTH)
}

fun dateStringToDate(dateString: String): Date {

    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
    val calendar = Calendar.getInstance()
    val currentTimestamp = System.currentTimeMillis()
    calendar.time = date

    calendar.set(Calendar.HOUR_OF_DAY, timestampToDate(currentTimestamp).hours)
    calendar.set(Calendar.MINUTE, timestampToDate(currentTimestamp).minutes)

    return calendar.time
}

fun getMockTask(): Task {
    val timestamp = System.currentTimeMillis()
    println("GET TS: $timestamp")
    return Task(
        dateStart = timestamp,
        dateFinish = timestamp + 1,
        name = "",
        description = ""
    )
}
