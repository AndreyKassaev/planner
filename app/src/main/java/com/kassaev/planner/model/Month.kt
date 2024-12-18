package com.kassaev.planner.model

import kotlinx.serialization.Serializable

@Serializable
data class Month(
    val previousMonthLastWeekDateList: List<CalendarDate>,
    val currentMonthDateList: List<CalendarDate>,
    val followingMonthFirstWeekDateList: List<CalendarDate>,
)
