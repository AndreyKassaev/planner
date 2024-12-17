package com.kassaev.planner.data.repository

import com.kassaev.planner.data.dao.MonthDao
import com.kassaev.planner.util.MonthGenerator
import com.kassaev.planner.util.MonthMapper.entityListToModelList
import com.kassaev.planner.util.MonthMapper.modelToEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class CalendarRepositoryImpl(
    private val monthDao: MonthDao
) : CalendarRepository {

    val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        scope.launch {
            initDb()
        }
    }

    override suspend fun initDb() {
        (-12..13).forEach { offset ->
            val calendar = Calendar.getInstance(Locale("ru"))
            calendar.add(Calendar.MONTH, offset)
            insertMonth(
                month = calendar.get(Calendar.MONTH),
                year = calendar.get(Calendar.YEAR)
            )
        }
    }

    override fun getMonthListFlow() =
        monthDao.getAll().map { entityListToModelList(it) }

    private suspend fun insertMonth(month: Int, year: Int) {
        monthDao.insertAll(
            modelToEntity(
                MonthGenerator.getMonth(
                    month = month,
                    year = year
                )
            )
        )
    }

    override suspend fun getMonthRowNumber(monthFirstDay: String): Int =
        monthDao.getMonthRowNumber(monthFirstDay)
}