package com.kassaev.planner.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kassaev.planner.data.repository.CalendarRepository
import com.kassaev.planner.model.Task
import com.kassaev.planner.util.formatDateWithoutTime
import com.kassaev.planner.util.getDayStartFinishTimestampPair
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class CalendarViewModel(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    private val pagerStateCurrentPageFlowMutable = MutableStateFlow(0)

    private val currentMonthIndexFlowMutable = MutableStateFlow(0)
    private val currentMonthIndexFlow: StateFlow<Int> = currentMonthIndexFlowMutable

    private val selectedDateFlowMutable = MutableStateFlow<String?>(null)
    private val selectedDateFlow: StateFlow<String?> = selectedDateFlowMutable

    private val taskListFlowMutable = MutableStateFlow(emptyList<Task>())
    private val taskListFlow: StateFlow<List<Task>> = taskListFlowMutable
    private val taskListFlowCombined = combine(
        selectedDateFlow,
        pagerStateCurrentPageFlowMutable,
        getMonthListFlow()
    ) { selectedDate, pagerCurrentPage, monthList ->
        monthList.isNotEmpty().let {
            Pair(selectedDate, pagerCurrentPage)
        }
    }

    init {
        setCurrentMonthIndex()
        viewModelScope.launch {
            taskListFlowCombined.collect { (selectedDate, pagerCurrentPage) ->
                setTaskList(selectedDate, pagerCurrentPage)
            }
        }
    }

    fun setPagerStateCurrentPage(currentPage: Int) {
        viewModelScope.launch {
            pagerStateCurrentPageFlowMutable.update {
                currentPage
            }
        }
    }

    fun getTaskListFlow() = taskListFlow

    fun getSelectedDateFlow() = selectedDateFlow

    fun setSelectedDate(date: String?) {
        viewModelScope.launch {
            selectedDateFlowMutable.update {
                date
            }
        }
    }

    fun getCurrentMonthIndexFlow() = currentMonthIndexFlow

    fun getMonthListFlow() = calendarRepository.getMonthListFlow()

    private fun setCurrentMonthIndex() {
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

    private fun setTaskList(selectedDate: String?, pagerCurrentPage: Int) {
        viewModelScope.launch {
            if (selectedDate != null) {
                //taskList of selected day
                val dayStartFinishTimestampPair =
                    getDayStartFinishTimestampPair(dateString = selectedDate)
                if (dayStartFinishTimestampPair != null) {
                    calendarRepository.getMonthTaskFlow(
                        dateStart = dayStartFinishTimestampPair.first,
                        dateFinish = dayStartFinishTimestampPair.second
                    ).collectLatest { selectedDayTaskList ->
                        taskListFlowMutable.update {
                            selectedDayTaskList
                        }
                    }
                }
            } else {
                //taskList of whole month
                val currentMonthDateList =
                    getMonthListFlow().first()[pagerStateCurrentPageFlowMutable.first()].currentMonthDateList
                val monthStartTimestamp =
                    getDayStartFinishTimestampPair(currentMonthDateList.first())?.first
                val monthFinishTimestamp =
                    getDayStartFinishTimestampPair(currentMonthDateList.last())?.second

                if (monthStartTimestamp != null && monthFinishTimestamp != null) {
                    calendarRepository.getMonthTaskFlow(
                        dateStart = monthStartTimestamp,
                        dateFinish = monthFinishTimestamp
                    ).collectLatest { wholeMonthTaskList ->
                        taskListFlowMutable.update {
                            wholeMonthTaskList
                        }
                    }
                }
            }
        }
    }
}
