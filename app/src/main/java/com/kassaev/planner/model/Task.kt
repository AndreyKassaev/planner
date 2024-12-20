package com.kassaev.planner.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Long = 0,
    val dateStart: Long,
    val dateFinish: Long,
    val name: String,
    val description: String
) {
    companion object {
        val mock = Task(
            dateStart = System.currentTimeMillis(),
            dateFinish = System.currentTimeMillis(),
            name = "",
            description = ""
        )
    }
}
