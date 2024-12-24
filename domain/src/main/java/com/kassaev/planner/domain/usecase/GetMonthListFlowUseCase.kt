package com.kassaev.planner.domain.usecase

import com.kassaev.planner.domain.model.Month
import com.kassaev.planner.domain.repository.CalendarRepository
import kotlinx.coroutines.flow.Flow

interface GetMonthListFlowUseCase {
    operator fun invoke(): Flow<List<Month>>
}

class GetMonthListFlowUseCaseImpl(
    private val repository: CalendarRepository
) : GetMonthListFlowUseCase {
    override fun invoke() =
        repository.getMonthListFlow()
}