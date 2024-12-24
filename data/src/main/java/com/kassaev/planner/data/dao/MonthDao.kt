package com.kassaev.planner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kassaev.planner.data.entity.Month
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg months: Month)

    @Query("SELECT * FROM month ORDER BY firstDay ASC")
    fun getAll(): Flow<List<Month>>

    @Update
    suspend fun updateMonth(month: Month)

    @Query("SELECT COUNT(*) AS row_num FROM month WHERE firstDay <= :monthFirstDay")
    suspend fun getMonthRowNumber(monthFirstDay: String): Int
}