package com.kassaev.planner.data.repository

import com.kassaev.planner.data.dao.MonthDao
import com.kassaev.planner.util.MonthGenerator
import com.kassaev.planner.util.MonthMapper.entityListToModelList
import com.kassaev.planner.util.MonthMapper.modelToEntity
import kotlinx.coroutines.flow.map
import java.util.Calendar

class CalendarRepositoryImpl(
    private val monthDao: MonthDao
) : CalendarRepository {

    override suspend fun initDb() {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        insertMonth(
            month = calendar.time.month,
            year = calendar.time.year
        )
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
}