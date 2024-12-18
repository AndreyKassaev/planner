package com.kassaev.planner.model

import kotlinx.serialization.Serializable

@Serializable
data class CalendarDate(
    val date: String, //YYYY-MM-DD
    val taskList: List<Task>
)
