package com.kassaev.planner.domain.usecase

import com.kassaev.planner.domain.model.Task
import com.kassaev.planner.domain.repository.CalendarRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class TaskTest {

    private lateinit var calendarRepository: CalendarRepository
    private lateinit var upsertTaskUseCase: UpsertTaskUseCase

    @BeforeEach
    fun setup() {
        calendarRepository = mock(CalendarRepository::class.java)
        upsertTaskUseCase = UpsertTaskUseCaseImpl(repository = calendarRepository)
    }

    @Test
    fun `upsertTaskUseCase executes correct repo method`() =
        runTest {
            val currentTimestamp = System.currentTimeMillis()
            val taskId = -1L
            val task = Task(
                id = taskId,
                dateStart = currentTimestamp,
                dateFinish = currentTimestamp + 1,
                name = "test",
                description = "test"
            )
            upsertTaskUseCase(task)
            verify(calendarRepository).upsertTask(task)
        }
}