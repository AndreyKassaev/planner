package com.kassaev.planner.data.entity

data class Month(
    val previousMonthLastWeekDateList: List<String>,
    val currentMonthDateList: List<String>,
    val followingMonthFirstWeekDateList: List<String>,
)
