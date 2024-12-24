package com.kassaev.planner.data.repository

import com.kassaev.planner.data.dao.MonthDao
import com.kassaev.planner.data.dao.TaskDao
import com.kassaev.planner.data.util.MonthGenerator
import com.kassaev.planner.data.util.MonthMapper
import com.kassaev.planner.data.util.TaskMapper
import com.kassaev.planner.domain.repository.CalendarRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.Locale
import com.kassaev.planner.domain.model.Task as TaskDomain

class CalendarRepositoryImpl(
    private val monthDao: MonthDao,
    private val taskDao: TaskDao
) : CalendarRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        runBlocking {
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

    override fun getMonthTaskFlow(dateStart: Long, dateFinish: Long): Flow<List<TaskDomain>> =
        taskDao.getMonthTaskFlow(dateStart = dateStart, dateFinish = dateFinish)
            .map { TaskMapper.entityListToDomainModelList(it) }

    override fun getMonthListFlow() =
        monthDao.getAll().map { MonthMapper.entityListToDomainModelList(it) }

    override suspend fun getMonthRowNumber(monthFirstDay: String): Int =
        monthDao.getMonthRowNumber(monthFirstDay)

    override fun upsertTask(task: TaskDomain) {
        scope.launch {
            taskDao.upsertTask(
                task = TaskMapper.domainModelToEntity(task)
            )
        }
    }

    override fun getTaskByIdFlow(id: Long): Flow<TaskDomain> =
        taskDao.getTaskById(taskId = id).map { TaskMapper.entityToDomainModel(it) }

    private suspend fun insertMonth(month: Int, year: Int) {
        monthDao.insertAll(
            MonthMapper.dataModelToEntity(
                MonthGenerator.getMonth(
                    month = month,
                    year = year
                )
            )
        )
    }
}