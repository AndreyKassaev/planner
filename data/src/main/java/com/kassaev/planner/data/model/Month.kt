package com.kassaev.planner.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Month(
    val previousMonthLastWeekDateList: List<String>,
    val currentMonthDateList: List<String>,
    val followingMonthFirstWeekDateList: List<String>,
)
