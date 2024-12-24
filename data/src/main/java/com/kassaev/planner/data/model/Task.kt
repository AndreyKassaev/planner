package com.kassaev.planner.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Long? = null,
    val dateStart: Long,
    val dateFinish: Long,
    val name: String,
    val description: String
)