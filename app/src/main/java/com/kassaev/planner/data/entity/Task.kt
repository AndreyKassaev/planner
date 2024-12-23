package com.kassaev.planner.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateStart: Long,
    val dateFinish: Long,
    val name: String,
    val description: String
)
