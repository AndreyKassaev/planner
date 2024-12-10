package com.kassaev.planner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kassaev.planner.data.entity.Month
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthDao {

    @Insert
    suspend fun insertAll(vararg months: Month)

    @Query("SELECT * FROM month")
    fun getAll(): Flow<List<Month>>

    @Update
    suspend fun updateMonth(month: Month)
}