package com.kassaev.planner.domain.usecase

import com.kassaev.planner.domain.repository.CalendarRepository

interface DeleteTaskByIdUseCase {
    operator fun invoke(taskId: Long)
}

internal class DeleteTaskByIdUseCaseImpl(
    private val repository: CalendarRepository
) : DeleteTaskByIdUseCase {
    override fun invoke(taskId: Long) {
        repository.deleteTaskById(taskId = taskId)
    }
}