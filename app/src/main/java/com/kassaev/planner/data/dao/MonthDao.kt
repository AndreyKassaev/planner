package com.kassaev.planner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kassaev.planner.data.entity.Month
import com.kassaev.planner.data.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg months: Month)

    @Transaction
    @Query("SELECT * FROM month ORDER BY firstDay ASC")
    fun getAll(): Flow<List<Month>>

    @Transaction
    @Update
    suspend fun updateMonth(month: Month)

    @Transaction
    @Query("SELECT COUNT(*) AS row_num FROM month WHERE firstDay <= :monthFirstDay")
    suspend fun getMonthRowNumber(monthFirstDay: String): Int

    @Transaction
    @Query("SELECT * FROM task WHERE dateStart <= :dateStart AND dateFinish >= :dateFinish ORDER BY dateStart ASC")
    fun getMonthTaskFlow(dateStart: Long, dateFinish: Long): Flow<List<Task>>
}