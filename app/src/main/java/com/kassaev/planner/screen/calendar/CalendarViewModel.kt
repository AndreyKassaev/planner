package com.kassaev.planner.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kassaev.planner.data.repository.CalendarRepository
import com.kassaev.planner.model.Month
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

class CalendarViewModel(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    private val monthListFlowMutable = MutableStateFlow(emptyList<Month>())
    private val monthListFlow: StateFlow<List<Month>> = monthListFlowMutable

    private val currentMonthIndexFlowMutable = MutableStateFlow(0)
    private val currentMonthIndexFlow: StateFlow<Int> = currentMonthIndexFlowMutable

    private val selectedDateFlowMutable = MutableStateFlow<String?>(null)
    private val selectedDateFlow: StateFlow<String?> = selectedDateFlowMutable

    @OptIn(ExperimentalCoroutinesApi::class)
    private val taskListFlow = combine(selectedDateFlow, monthListFlow) { selectedDate, monthList ->
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
                    monthList[currentMonthIndexFlow.first()].currentMonthDateList
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

    fun getTaskListFlow() = taskListFlow

    init {
        getCurrentMonthIndex()
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