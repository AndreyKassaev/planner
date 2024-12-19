package com.kassaev.planner.util

import java.util.Calendar
import java.util.Locale
import com.kassaev.planner.model.Month as MonthModel

object MonthGenerator {

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

    private fun getFollowingMonthFirstWeekDateList(
        currentMonth: Int,
        year: Int
    ): List<String> {
        val calendar =
            Calendar.getInstance(Locale("ru")) //required for Monday to be the first day of week
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, currentMonth + 1)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val currentWeek = calendar.get(Calendar.WEEK_OF_MONTH)
        val firstWeekDayList = mutableListOf<String>()
        for (i in 1..7) {
            if (calendar.get(Calendar.WEEK_OF_MONTH) != currentWeek) {
                break
            }
            firstWeekDayList.add(
                formatDateWithoutTime(calendar.time),
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return firstWeekDayList
    }

    private fun getCurrentMonthDateList(currentMonth: Int, year: Int): List<String> {
        val calendar = Calendar.getInstance(Locale("ru"))
        calendar.apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, currentMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val currentMontDateList = mutableListOf<String>()
        repeat(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            currentMontDateList.add(
                formatDateWithoutTime(calendar.time),
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return currentMontDateList
    }

    private fun getPreviousMonthLastWeekDateList(currentMonth: Int, year: Int): List<String> {
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

        val lastWeekDateList = mutableListOf<String>()
        for (i in 0 until 7) {
            if (previousMonth != calendar.time.month) {
                break
            }
            lastWeekDateList.add(
                formatDateWithoutTime(calendar.time),
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return lastWeekDateList
    }
}