package com.kassaev.planner.data.repository

import com.kassaev.planner.model.Month
import com.kassaev.planner.model.Task
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {

    suspend fun initDb()

    fun getMonthListFlow(): Flow<List<Month>>

    suspend fun getMonthRowNumber(monthFirstDay: String): Int

    fun getMonthTaskFlow(dateStart: Long, dateFinish: Long): Flow<List<Task>>

    suspend fun upsertTask(task: Task)
}