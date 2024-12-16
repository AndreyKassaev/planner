package com.kassaev.planner.screen.calendar

import androidx.lifecycle.ViewModel
import com.kassaev.planner.data.repository.CalendarRepository

class CalendarViewModel(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    fun getCalendarFlow() = calendarRepository.getMonthListFlow()
}