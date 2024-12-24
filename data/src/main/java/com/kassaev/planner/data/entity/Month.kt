package com.kassaev.planner.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Month(
    @PrimaryKey
    val firstDay: String,
    val data: String
)
