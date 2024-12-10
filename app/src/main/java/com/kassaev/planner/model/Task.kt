package com.kassaev.planner.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: String,
    val dateStart: String,
    val dateFinish: String,
    val name: String,
    val description: String
)
