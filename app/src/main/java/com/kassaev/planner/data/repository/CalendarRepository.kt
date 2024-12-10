package com.kassaev.planner.data.repository

import com.kassaev.planner.model.Month
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {

    suspend fun initDb()

    fun getMonthListFlow(): Flow<List<Month>>
}