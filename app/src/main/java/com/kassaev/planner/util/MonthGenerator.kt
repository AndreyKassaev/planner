package com.kassaev.planner.util

import com.kassaev.planner.model.CalendarDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.kassaev.planner.model.Month as MonthModel

object MonthGenerator {

    //prefer room flow
//    private fun getFilledCalendarMapList(): MutableList<Map<String, List<Date>>> {
//
//        val filledCalendarMapList = mutableListOf<Map<String, List<Date>>>()
//
//        val calendar = Calendar.getInstance()
//        val currentDate = calendar.time
//
//        repeat((-3..3).count()) { i ->
//            filledCalendarMapList.add(
//                getFilledCalendarMap(
//                    currentMonth = currentDate.month + (i)
//                )
//            )
//        }
//        return filledCalendarMapList
//    }

    //Map -> data class
//    fun getFilledCalendarMap(currentMonth: Int): Map<String, List<Date>> =
//        mapOf(
//            "previousMonthLastWeekDateList" to getPreviousMonthLastWeekDateList(
//                currentMonth = currentMonth
//            ),
//            "currentMonthDateList" to getCurrentMonthDateList(
//                currentMonth = currentMonth
//            ),
//            "followingMonthFirstWeekDateList" to getFollowingMonthFirstWeekDateList(
//                currentMonth = currentMonth
//            )
//        )


    //    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//    val date: Date = dateFormat.parse("2024-12-31") ?: Date()
//    println(dateFormat.format(date))
    //UI
    private fun formatDateWithoutTime(date: Date): String {
        val result = SimpleDateFormat("yyyy-MM-dd", Locale("ru")).format(date)
        return result
    }

//    private fun getCalendarDateTextView(date: Date, isCurrent: Boolean = false): TextView {
//        val currentDate = Calendar.getInstance().time
//        val textView = TextView(requireContext())
//        textView.setPadding(32, 32, 32, 32)
//        textView.textSize = 24F
//        textView.alpha = if(isCurrent) 1F else 0.25F
//        textView.text = date.date.toString()
//        if (formatDateWithoutTime(date) == formatDateWithoutTime(currentDate)) textView.setTextColor(
//            ContextCompat.getColor(requireContext(), R.color.calendar))
//        return textView
//    }


    fun getMonth(month: Int, year: Int): MonthModel =
        MonthModel(
            previousMonthLastWeekDateList = getPreviousMonthLastWeekDateList(
                currentMonth = month,
                year = year
            ),
            currentMonthDateList = getCurrentMonthDateList(
                currentMonth = month,
                year = year
            ),
            followingMonthFirstWeekDateList = getFollowingMonthFirstWeekDateList(
                currentMonth = month,
                year = year
            )
        )


    private fun monthToInt(monthEnum: MonthEnum): Int =
        when (monthEnum) {
            MonthEnum.JANUARY -> 0
            MonthEnum.FEBRUARY -> 1
            MonthEnum.MARCH -> 2
            MonthEnum.APRIL -> 3
            MonthEnum.MAY -> 4
            MonthEnum.JUNE -> 5
            MonthEnum.JULY -> 6
            MonthEnum.AUGUST -> 7
            MonthEnum.SEPTEMBER -> 8
            MonthEnum.OCTOBER -> 8
            MonthEnum.NOVEMBER -> 10
            MonthEnum.DECEMBER -> 11
        }

    private fun getFollowingMonthFirstWeekDateList(
        currentMonth: Int,
        year: Int
    ): List<CalendarDate> {
        val calendar = Calendar.getInstance(Locale("ru"))
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, currentMonth + 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val currentWeek = calendar.get(Calendar.WEEK_OF_MONTH)
        val firstWeekDayList = mutableListOf<CalendarDate>()
        for (i in 1..7) {
            if (calendar.get(Calendar.WEEK_OF_MONTH) != currentWeek) {
                break
            }
            firstWeekDayList.add(
                CalendarDate(
                    date = formatDateWithoutTime(calendar.time).toString(),
                    taskList = emptyList()
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return firstWeekDayList
    }

    private fun getCurrentMonthDateList(currentMonth: Int, year: Int): List<CalendarDate> {
        val calendar = Calendar.getInstance(Locale("ru"))
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, currentMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val currentMontDateList = mutableListOf<CalendarDate>()
        repeat(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            currentMontDateList.add(
                CalendarDate(
                    date = formatDateWithoutTime(calendar.time).toString(),
                    taskList = emptyList()
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return currentMontDateList
    }

    private fun getPreviousMonthLastWeekDateList(currentMonth: Int, year: Int): List<CalendarDate> {
        val calendar = Calendar.getInstance(Locale("ru"))
        val previousMonth = if (currentMonth == 0) 11 else currentMonth - 1
        val yearVal = if (currentMonth == 0) year - 1 else year

        calendar.apply {
            set(Calendar.YEAR, yearVal)
            set(Calendar.MONTH, previousMonth)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        calendar.time //REQUIRED! for conjunction with following line otherwise not working.
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val lastWeekDateList = mutableListOf<CalendarDate>()
        for (i in 0 until 7) {
            if (previousMonth != calendar.time.month) {
                break
            }
            lastWeekDateList.add(
                CalendarDate(
                    date = formatDateWithoutTime(calendar.time),
                    taskList = emptyList()
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return lastWeekDateList
    }
}