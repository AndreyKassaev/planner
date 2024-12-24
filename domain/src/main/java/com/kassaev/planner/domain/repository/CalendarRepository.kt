package com.kassaev.planner.domain.repository


import com.kassaev.planner.domain.model.Month
import com.kassaev.planner.domain.model.Task
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {

    fun getMonthListFlow(): Flow<List<Month>>

    fun getMonthRowNumberDeferred(monthFirstDay: String): Deferred<Int>

    fun getTaskListFlow(dateStart: Long, dateFinish: Long): Flow<List<Task>>

    fun upsertTask(task: Task)

    fun getTaskByIdFlow(id: Long): Flow<Task>
}