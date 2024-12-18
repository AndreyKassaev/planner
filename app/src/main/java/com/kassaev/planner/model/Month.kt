package com.kassaev.planner.model

import kotlinx.serialization.Serializable

@Serializable
data class Month(
    val previousMonthLastWeekDateList: List<String>,
    val currentMonthDateList: List<String>,
    val followingMonthFirstWeekDateList: List<String>,
)
