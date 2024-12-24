package com.kassaev.planner.data.repository

import com.kassaev.planner.data.dao.MonthDao
import com.kassaev.planner.data.dao.TaskDao
import com.kassaev.planner.data.util.MonthGenerator
import com.kassaev.planner.data.util.MonthMapper
import com.kassaev.planner.data.util.TaskMapper
import com.kassaev.planner.domain.repository.CalendarRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.Locale
import com.kassaev.planner.domain.model.Task as TaskDomain

internal class CalendarRepositoryImpl(
    private val monthDao: MonthDao,
    private val taskDao: TaskDao
) : CalendarRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        setMonthList()
    }

    override fun getTaskListFlow(dateStart: Long, dateFinish: Long): Flow<List<TaskDomain>> =
        taskDao.getTaskListFlow(dateStart = dateStart, dateFinish = dateFinish)
            .map { TaskMapper.entityListToDomainModelList(it) }

    override fun getMonthListFlow() =
        monthDao.getMonthListFlow().map { MonthMapper.entityListToDomainModelList(it) }

    override fun getMonthRowNumberDeferred(monthFirstDay: String): Deferred<Int> =
        scope.async {
            monthDao.getMonthRowNumber(monthFirstDay)
        }

    override fun upsertTask(task: TaskDomain) {
        scope.launch {
            taskDao.upsertTask(
                task = TaskMapper.domainModelToEntity(task)
            )
        }
    }

    override fun getTaskByIdFlow(id: Long): Flow<TaskDomain> =
        taskDao.getTaskByIdFlow(taskId = id).map { TaskMapper.entityToDomainModel(it) }

    private fun setMonthList() {
        runBlocking {
            (-12..12).forEach { offset ->
                val calendar = Calendar.getInstance(Locale("ru"))
                calendar.add(Calendar.MONTH, offset)
                insertMonth(
                    month = calendar.get(Calendar.MONTH),
                    year = calendar.get(Calendar.YEAR)
                )
            }
        }
    }

    private suspend fun insertMonth(month: Int, year: Int) {
        monthDao.insertMonth(
            MonthMapper.dataModelToEntity(
                MonthGenerator.getMonth(
                    month = month,
                    year = year
                )
            )
        )
    }
}