package com.kassaev.planner.domain.model

data class Month(
    val previousMonthLastWeekDateList: List<String>,
    val currentMonthDateList: List<String>,
    val followingMonthFirstWeekDateList: List<String>,
)