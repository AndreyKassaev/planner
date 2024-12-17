package com.kassaev.planner.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kassaev.planner.data.repository.CalendarRepository
import com.kassaev.planner.model.CalendarDate
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

    private val selectedDateFlowMutable = MutableStateFlow(CalendarDate.mock)
    private val selectedDateFlow: StateFlow<CalendarDate> = selectedDateFlowMutable

    init {
        getMonthRowNumber()
    }

    fun getSelectedDateFlow() = selectedDateFlow

    fun setSelectedDate(date: CalendarDate) {
        viewModelScope.launch {
            selectedDateFlowMutable.update {
                date
            }
        }
    }

    fun getMonthRowNumberFlow() = currentMonthRowNumberFlow

    fun getCalendarFlow() = calendarRepository.getMonthListFlow()

    private fun getMonthRowNumber() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val currentMonthFirstDay = formatDateWithoutTime(calendar.time)
        viewModelScope.launch {
            currentMonthRowNumberFlowMutable.update {
                calendarRepository.getMonthRowNumber(monthFirstDay = currentMonthFirstDay)
            }
        }
    }
}