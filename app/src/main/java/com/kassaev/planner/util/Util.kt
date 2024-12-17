package com.kassaev.planner.util

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kassaev.planner.R
import com.kassaev.planner.model.CalendarDate
import com.kassaev.planner.model.Month
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getCalendarDateTextView(
    date: CalendarDate,
    isCurrent: Boolean = false,
    context: Context
): TextView {
    val currentDate = Calendar.getInstance().time
    val textView = TextView(context)
    textView.setPadding(32, 32, 32, 32)
    textView.textSize = 24F
    textView.alpha = if (isCurrent) 1F else 0.25F
    textView.text = getDay(date)
    textView.setOnClickListener {
        println(date.date)
    }
    if (formatDateWithoutTime(SimpleDateFormat("yyyy-MM-dd").parse(date.date)) == formatDateWithoutTime(
            currentDate
        )
    ) textView.setTextColor(
        ContextCompat.getColor(context, R.color.calendar)
    )
    return textView
}

fun isToday(date: CalendarDate): Boolean =
    formatDateWithoutTime(SimpleDateFormat("yyyy-MM-dd").parse(date.date)) == formatDateWithoutTime(
        Calendar.getInstance().time
    )


fun getDay(date: CalendarDate) = date.date.split("-").last().toInt().toString()

fun formatDateWithoutTime(date: Date) =
    SimpleDateFormat("dd-MM-yyyy", Locale("ru")).format(date)

fun getMonthResourceId(month: Month) =
    when (month.currentMonthDateList.first().date.split("-")[1].toInt()) {
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
    month.currentMonthDateList.first().date.split("-").first()
