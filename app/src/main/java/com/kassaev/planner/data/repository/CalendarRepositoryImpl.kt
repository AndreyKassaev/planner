package com.kassaev.planner.data.repository

import com.kassaev.planner.data.dao.MonthDao
import com.kassaev.planner.model.Task
import com.kassaev.planner.util.MonthGenerator
import com.kassaev.planner.util.MonthMapper
import com.kassaev.planner.util.TaskMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

class CalendarRepositoryImpl(
    private val monthDao: MonthDao
) : CalendarRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        scope.launch {
            initDb()
        }
    }

    override suspend fun initDb() {
        (-12..12).forEach { offset ->
            val calendar = Calendar.getInstance(Locale("ru"))
            calendar.add(Calendar.MONTH, offset)
            insertMonth(
                month = calendar.get(Calendar.MONTH),
                year = calendar.get(Calendar.YEAR)
            )
        }
    }

    override fun getMonthTaskFlow(dateStart: Long, dateFinish: Long): Flow<List<Task>> =
        monthDao.getMonthTaskFlow(dateStart = dateStart, dateFinish = dateFinish)
            .map { TaskMapper.entityListToModelList(it) }

    override fun getMonthListFlow() =
        monthDao.getAll().map { MonthMapper.entityListToModelList(it) }


    override suspend fun getMonthRowNumber(monthFirstDay: String): Int =
        monthDao.getMonthRowNumber(monthFirstDay)

    override suspend fun upsertTask(task: Task) {
        monthDao.upsertTask(
            task = TaskMapper.modelToEntity(task)
        )
    }

    override fun getTaskByIdFlow(id: Long): Flow<Task> =
        monthDao.getTaskById(taskId = id).map { TaskMapper.entityToModel(it) }

    private suspend fun insertMonth(month: Int, year: Int) {
        monthDao.insertAll(
            MonthMapper.modelToEntity(
                MonthGenerator.getMonth(
                    month = month,
                    year = year
                )
            )
        )
    }
}