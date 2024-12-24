package com.kassaev.planner.domain.usecase

import com.kassaev.planner.domain.model.Task
import com.kassaev.planner.domain.repository.CalendarRepository

interface UpsertTaskUseCase {
    operator fun invoke(task: Task)
}

internal class UpsertTaskUseCaseImpl(
    private val repository: CalendarRepository
) : UpsertTaskUseCase {
    override fun invoke(task: Task) {
        repository.upsertTask(task = task)
    }
}