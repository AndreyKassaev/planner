package com.kassaev.planner.domain.usecase

import com.kassaev.planner.domain.model.Task
import com.kassaev.planner.domain.repository.CalendarRepository
import kotlinx.coroutines.flow.Flow

interface GetTaskListFlowUseCase {
    operator fun invoke(dateStart: Long, dateFinish: Long): Flow<List<Task>>
}

internal class GetTaskListFlowUseCaseImpl(
    private val repository: CalendarRepository
) : GetTaskListFlowUseCase {
    override fun invoke(dateStart: Long, dateFinish: Long) =
        repository.getTaskListFlow(
            dateStart = dateStart,
            dateFinish = dateFinish
        )
}
