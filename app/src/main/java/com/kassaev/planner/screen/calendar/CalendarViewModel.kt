package com.kassaev.planner.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kassaev.planner.data.entity.Task
import com.kassaev.planner.data.repository.CalendarRepository
import com.kassaev.planner.util.formatDateWithoutTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class CalendarViewModel(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    private val currentMonthIndexFlowMutable = MutableStateFlow(0)
    private val currentMonthIndexFlow: StateFlow<Int> = currentMonthIndexFlowMutable

    private val selectedDateFlowMutable = MutableStateFlow<String?>(null)
    private val selectedDateFlow: StateFlow<String?> = selectedDateFlowMutable

    private val taskListFlowMutable = MutableStateFlow(emptyList<Task>())
    private val taskListFlow: StateFlow<List<Task>> = taskListFlowMutable

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

    fun getMonthTaskFlow(dateStart: Long, dateFinish: Long) =
        calendarRepository.getMonthTaskFlow(dateStart = dateStart, dateFinish = dateFinish)

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