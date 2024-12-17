package com.kassaev.planner.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val currentMonthRowNumberFlowMutable = MutableStateFlow(0)
    private val currentMonthRowNumberFlow: StateFlow<Int> = currentMonthRowNumberFlowMutable

    init {
        getMonthRowNumber()
    }

    fun getMonthRowNumberFlow() = currentMonthRowNumberFlow

    fun getCalendarFlow() = calendarRepository.getMonthListFlow()

    private fun getMonthRowNumber() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val currentMonthFirstDay = formatDateWithoutTime(calendar.time)
        println("currentMonthFirstDay: $currentMonthFirstDay")
        viewModelScope.launch {
            val result = calendarRepository.getMonthRowNumber(monthFirstDay = currentMonthFirstDay)
            println("OFFSET: $result")
            currentMonthRowNumberFlowMutable.update {
                result
            }
        }
    }
}