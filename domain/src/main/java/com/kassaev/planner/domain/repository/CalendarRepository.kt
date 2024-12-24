package com.kassaev.planner.domain.repository


import com.kassaev.planner.domain.model.Month
import com.kassaev.planner.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {

    suspend fun initDb()

    fun getMonthListFlow(): Flow<List<Month>>

    suspend fun getMonthRowNumber(monthFirstDay: String): Int

    fun getMonthTaskFlow(dateStart: Long, dateFinish: Long): Flow<List<Task>>

    suspend fun upsertTask(task: Task)

    fun getTaskByIdFlow(id: Long): Flow<Task>
}