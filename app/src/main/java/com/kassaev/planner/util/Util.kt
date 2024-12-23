package com.kassaev.planner.util

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import com.kassaev.planner.R
import com.kassaev.planner.model.Month
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


fun hourMinutePairToTimestamp(hourMinutePair: Pair<Int, Int>): Long {
    val calendar = Calendar.getInstance()

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

fun dateStringToDate(dateString: String) =
    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)

data class TaskTime(
    val start: Pair<Int?, Int?>,
    val finish: Pair<Int?, Int?>,
) {
    companion object {
        val mock = TaskTime(
            start = Pair(null, null),
            finish = Pair(null, null)
        )
    }
}

//@Suppress(names = {"AutoBoxing"}) initialSelectedDateMillis: Long? = null,
//@Suppress(names = {"AutoBoxing"}) initialDisplayedMonthMillis: Long? = initialSelectedDateMillis,
//yearRange: IntRange = DatePickerDefaults.YearRange,
//initialDisplayMode: DisplayMode = DisplayMode.Picker,
//selectableDates: SelectableDates = DatePickerDefaults.AllDates

@OptIn(ExperimentalMaterial3Api::class)
class TaskDatePicker(

) : DatePickerState {
    override var displayMode: DisplayMode
        get() = TODO("Not yet implemented")
        set(value) {}
    override var displayedMonthMillis: Long
        get() = TODO("Not yet implemented")
        set(value) {}
    override val selectableDates: SelectableDates
        get() = TODO("Not yet implemented")
    override var selectedDateMillis: Long?
        get() = TODO("Not yet implemented")
        set(value) {}
    override val yearRange: IntRange
        get() = TODO("Not yet implemented")

}