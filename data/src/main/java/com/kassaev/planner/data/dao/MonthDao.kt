package com.kassaev.planner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kassaev.planner.data.entity.Month
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMonth(month: Month)

    @Query("SELECT * FROM month ORDER BY firstDay ASC")
    fun getMonthListFlow(): Flow<List<Month>>

    @Query("SELECT COUNT(*) AS row_num FROM month WHERE firstDay <= :monthFirstDay")
    suspend fun getMonthRowNumber(monthFirstDay: String): Int
}