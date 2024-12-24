package com.kassaev.planner.domain.model

data class Task(
    val id: Long? = null,
    val dateStart: Long,
    val dateFinish: Long,
    val name: String,
    val description: String
)
