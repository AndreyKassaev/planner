package com.kassaev.planner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kassaev.planner.data.entity.Month
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg months: Month)

    @Transaction
    @Query("SELECT * FROM month ORDER BY firstDay ASC")
    fun getAll(): Flow<List<Month>>

    @Transaction
    @Update
    suspend fun updateMonth(month: Month)
}