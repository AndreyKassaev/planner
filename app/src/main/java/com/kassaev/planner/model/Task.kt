package com.kassaev.planner.model

data class Task(
    val id: String,
    val dateStart: String,
    val dateFinish: String,
    val name: String,
    val description: String
)
