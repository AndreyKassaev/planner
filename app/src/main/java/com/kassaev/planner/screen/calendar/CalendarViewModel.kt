package com.kassaev.planner.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kassaev.planner.data.entity.Task
import com.kassaev.planner.data.repository.CalendarRepository
import com.kassaev.planner.util.formatDateWithoutTime
import com.kassaev.planner.util.getDayStartFinishTimestampPair
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.random.Random

class CalendarViewModel(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    private val pagerStateCurrentPageFlowMutable = MutableStateFlow(0)

    private val currentMonthIndexFlowMutable = MutableStateFlow(0)
    private val currentMonthIndexFlow: StateFlow<Int> = currentMonthIndexFlowMutable

    private val selectedDateFlowMutable = MutableStateFlow<String?>(null)
    private val selectedDateFlow: StateFlow<String?> = selectedDateFlowMutable

    @OptIn(ExperimentalCoroutinesApi::class)
    private val taskListFlow = combine(
        selectedDateFlow,
        getCalendarFlow(),
        pagerStateCurrentPageFlowMutable
    ) { selectedDate, monthList, pagerStateCurrentPage ->
        if (selectedDate != null) {
            val dayStartFinishTimestampPair =
                getDayStartFinishTimestampPair(dateString = selectedDate)
            if (dayStartFinishTimestampPair != null) {
                calendarRepository.getMonthTaskFlow(
                    dateStart = dayStartFinishTimestampPair.first,
                    dateFinish = dayStartFinishTimestampPair.second
                )
            } else {
                emptyFlow()
            }
        } else {
            if (monthList.isNotEmpty()) {
                val currentMonthDateList =
                    monthList[pagerStateCurrentPageFlowMutable.first()].currentMonthDateList
                val monthStartTimestamp =
                    getDayStartFinishTimestampPair(currentMonthDateList.first())?.first
                val monthFinishTimestamp =
                    getDayStartFinishTimestampPair(currentMonthDateList.last())?.second

                if (monthStartTimestamp != null && monthFinishTimestamp != null) {
                    calendarRepository.getMonthTaskFlow(
                        dateStart = monthStartTimestamp,
                        dateFinish = monthFinishTimestamp
                    )
                } else {
                    emptyFlow()
                }
            } else {
                emptyFlow()
            }
        }
    }.flatMapMerge { it }

    init {
        getCurrentMonthIndex()
    }

    fun setPagerStateCurrentPage(currentPage: Int) {
        viewModelScope.launch {
            pagerStateCurrentPageFlowMutable.update {
                currentPage
            }
        }
    }

    fun getTaskListFlow() = taskListFlow

    fun addMockTask() {
        viewModelScope.launch {
            calendarRepository.upsertTask(
                Task(
                    id = Random.nextLong(),
                    dateStart = System.currentTimeMillis(),
                    dateFinish = System.currentTimeMillis() + 1001,
                    name = "name",
                    description = "description"
                )
            )
        }
    }

    fun getSelectedDateFlow() = selectedDateFlow

    fun setSelectedDate(date: String?) {
        viewModelScope.launch {
            selectedDateFlowMutable.update {
                date
            }
        }
    }

    fun getCurrentMonthIndexFlow() = currentMonthIndexFlow

    fun getCalendarFlow() = calendarRepository.getMonthListFlow()

    private fun getCurrentMonthIndex() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val currentMonthFirstDay = formatDateWithoutTime(calendar.time)
        viewModelScope.launch {
            val monthRowNumber =
                calendarRepository.getMonthRowNumber(monthFirstDay = currentMonthFirstDay)
            currentMonthIndexFlowMutable.update {
                if (monthRowNumber != 0) monthRowNumber - 1 else 0
            }
        }
    }
}