package com.kassaev.planner.navigation

import kotlinx.serialization.Serializable

@Serializable
data object CalendarMainScreen

@Serializable
data class TaskDetail(
    val taskId: Long? = null
)