package com.kassaev.planner.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kassaev.planner.data.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task WHERE dateStart >= :dateStart AND dateFinish <= :dateFinish ORDER BY dateStart ASC")
    fun getTaskListFlow(dateStart: Long, dateFinish: Long): Flow<List<Task>>

    @Upsert
    suspend fun upsertTask(task: Task)

    @Query("SELECT * FROM task WHERE id = :taskId")
    fun getTaskByIdFlow(taskId: Long): Flow<Task>

    @Query("DELETE FROM task WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long)
}