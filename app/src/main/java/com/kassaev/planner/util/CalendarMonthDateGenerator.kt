package com.kassaev.planner.util

import java.util.Calendar
import java.util.Date
import java.util.Locale

object CalendarMonthDateGenerator {

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
    fun getFilledCalendarMap(currentMonth: Int): Map<String, List<Date>> =
        mapOf(
            "previousMonthLastWeekDateList" to getPreviousMonthLastWeekDateList(
                currentMonth = currentMonth
            ),
            "currentMonthDateList" to getCurrentMonthDateList(
                currentMonth = currentMonth
            ),
            "followingMonthFirstWeekDateList" to getFollowingMonthFirstWeekDateList(
                currentMonth = currentMonth
            )
        )

    //UI
//    private fun formatDateWithoutTime(date: Date) =
//        SimpleDateFormat("dd-MM-yyyy", Locale("ru")).format(date)

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


    private fun getFollowingMonthFirstWeekDateList(currentMonth: Int): List<Date> {

        val calendar = Calendar.getInstance(Locale("ru"))
        calendar.apply {
            set(Calendar.MONTH, currentMonth + 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val currentWeek = calendar.get(Calendar.WEEK_OF_MONTH)
        val firstWeekDayList = mutableListOf<Date>()

        for (i in 1..7) {
            if (calendar.get(Calendar.WEEK_OF_MONTH) != currentWeek) {
                break
            }
            firstWeekDayList.add(
                calendar.time
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return firstWeekDayList
    }

    private fun getCurrentMonthDateList(currentMonth: Int): List<Date> {

        val calendar = Calendar.getInstance(Locale("ru"))
        calendar.apply {
            set(Calendar.MONTH, currentMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val currentMontDateList = mutableListOf<Date>()

        repeat(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            currentMontDateList.add(
                calendar.time
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return currentMontDateList
    }

    private fun getPreviousMonthLastWeekDateList(currentMonth: Int): List<Date> {

        val calendar = Calendar.getInstance(Locale("ru"))
        val previousMonth = if (currentMonth == 0) 11 else currentMonth - 1 //months zero-based
        val yearVal = if (currentMonth == 0) calendar.get(Calendar.YEAR) - 1 else calendar.get(
            Calendar.YEAR
        ) //months zero-based

        calendar.apply {
            set(Calendar.YEAR, yearVal)
            set(Calendar.MONTH, previousMonth)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        calendar.time //REQUIRED! for conjunction with following line otherwise not working.
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val lastWeekDateList = mutableListOf<Date>()
        for (i in 0 until 7) {
            if (previousMonth != calendar.time.month) {
                break
            }
            lastWeekDateList.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return lastWeekDateList
    }
}