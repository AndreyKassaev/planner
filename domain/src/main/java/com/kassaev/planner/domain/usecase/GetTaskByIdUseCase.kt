package com.kassaev.planner.domain.usecase

import com.kassaev.planner.domain.model.Task
import com.kassaev.planner.domain.repository.CalendarRepository
import kotlinx.coroutines.flow.Flow

interface GetTaskByIdUseCase {
    operator fun invoke(taskId: Long): Flow<Task>
}

class GetTaskByIdUseCaseImpl(
    private val repository: CalendarRepository
) : GetTaskByIdUseCase {
    override fun invoke(taskId: Long): Flow<Task> =
        repository.getTaskByIdFlow(id = taskId)
}