package com.kassaev.planner.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kassaev.planner.data.repository.CalendarRepository
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            calendarRepository.initDb()
        }
//        viewModelScope.launch {
//            calendarRepository.getMonthListFlow().collect{ monthList ->
//                println(
//                    monthList.first().currentMonthDateList.first().date
//                )
//            }
//        }
    }


    fun initDb() {
        println("INIT")
    }
}