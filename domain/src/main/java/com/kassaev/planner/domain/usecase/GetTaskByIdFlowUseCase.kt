package com.kassaev.planner.domain.usecase

import com.kassaev.planner.domain.model.Task
import com.kassaev.planner.domain.repository.CalendarRepository
import kotlinx.coroutines.flow.Flow

interface GetTaskByIdFlowUseCase {
    operator fun invoke(taskId: Long): Flow<Task>
}

class GetTaskByIdFlowUseCaseImpl(
    private val repository: CalendarRepository
) : GetTaskByIdFlowUseCase {
    override fun invoke(taskId: Long): Flow<Task> =
        repository.getTaskByIdFlow(id = taskId)
}