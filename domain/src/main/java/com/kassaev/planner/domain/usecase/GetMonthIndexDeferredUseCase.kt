package com.kassaev.planner.domain.usecase

import com.kassaev.planner.domain.repository.CalendarRepository
import kotlinx.coroutines.Deferred

interface GetMonthIndexDeferredUseCase {
    operator fun invoke(monthFirstDay: String): Deferred<Int>
}

class GetMonthIndexDeferredUseCaseImpl(
    private val repository: CalendarRepository
) : GetMonthIndexDeferredUseCase {
    override fun invoke(monthFirstDay: String) =
        repository.getMonthRowNumberDeferred(monthFirstDay = monthFirstDay)
}