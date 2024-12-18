package com.kassaev.planner.data.entity

import androidx.room.Entity

@Entity
data class Task(
    val id: Long,
    val dateStart: Long,
    val dateFinish: Long,
    val name: String,
    val description: String
)
