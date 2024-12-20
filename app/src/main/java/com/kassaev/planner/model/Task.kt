package com.kassaev.planner.model

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class Task(
    val id: Long,
    val dateStart: Long,
    val dateFinish: Long,
    val name: String,
    val description: String
) {
    companion object {
        val mock = Task(
            id = Random.nextLong(),
            dateStart = System.currentTimeMillis(),
            dateFinish = System.currentTimeMillis(),
            name = "",
            description = ""
        )
    }
}
