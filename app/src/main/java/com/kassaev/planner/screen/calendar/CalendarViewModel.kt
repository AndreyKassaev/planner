package com.kassaev.planner.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kassaev.planner.domain.usecase.GetMonthIndexDeferredUseCase
import com.kassaev.planner.domain.usecase.GetMonthListFlowUseCase
import com.kassaev.planner.domain.usecase.GetTaskListFlowUseCase
import com.kassaev.planner.model.Task
import com.kassaev.planner.util.MonthMapper
import com.kassaev.planner.util.TaskMapper
import com.kassaev.planner.util.dateStringToDate
import com.kassaev.planner.util.formatDateWithoutTime
import com.kassaev.planner.util.getDayStartFinishTimestampPair
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class CalendarViewModel(
    private val getMonthListFlowUseCase: GetMonthListFlowUseCase,
    private val getMonthIndexDeferredUseCase: GetMonthIndexDeferredUseCase,
    private val getTaskListFlowUseCase: GetTaskListFlowUseCase
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

    private val busyDateListFlowMutable = MutableStateFlow<List<String>>(emptyList())
    private val busyDateListFlow: StateFlow<List<String>> = busyDateListFlowMutable

    init {
        setCurrentMonthIndex()
        viewModelScope.launch {
            taskListFlowCombined.collect { (selectedDate, pagerCurrentPage) ->
                setTaskList(selectedDate, pagerCurrentPage)
            }
        }
        setBusyDateListFlow()
    }

    fun getBusyDateListFlow() = busyDateListFlow

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

    fun getMonthListFlow() =
        getMonthListFlowUseCase().map { MonthMapper.domainModelListToUiModelList(it) }

    private fun setCurrentMonthIndex() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val currentMonthFirstDay = formatDateWithoutTime(calendar.time)
        viewModelScope.launch {
            val monthRowNumber =
                getMonthIndexDeferredUseCase(monthFirstDay = currentMonthFirstDay)
                    .await()
            currentMonthIndexFlowMutable.update {
                if (monthRowNumber != 0) monthRowNumber - 1 else 0
            }
        }
    }

    private fun setBusyDateListFlow() {
        viewModelScope.launch {
            pagerStateCurrentPageFlowMutable.collectLatest { currentMonthIndex ->
                getMonthListFlow().collectLatest { monthList ->
                    monthList[currentMonthIndex].currentMonthDateList.let { currentMonthDateList ->
                        val monthStartTimestamp =
                            getDayStartFinishTimestampPair(currentMonthDateList.first())?.first
                        val monthFinishTimestamp =
                            getDayStartFinishTimestampPair(currentMonthDateList.last())?.second

                        if (monthStartTimestamp != null && monthFinishTimestamp != null) {
                            getTaskListFlowUseCase(
                                dateStart = monthStartTimestamp,
                                dateFinish = monthFinishTimestamp
                            ).collectLatest { wholeMonthTaskList ->
                                getMonthListFlowUseCase().collectLatest { monthList ->
                                    val dateList =
                                        monthList[currentMonthIndexFlow.first()].currentMonthDateList.map {
                                            dateStringToDate(
                                                it
                                            )
                                        }
                                    val taskTimestampList = wholeMonthTaskList.map { it.dateStart }
                                    busyDateListFlowMutable.update {
                                        dateList.filter { date ->
                                            taskTimestampList.map { Date(it) }.any { timestampDate ->
                                                val calendar1 = Calendar.getInstance().apply { time = date }
                                                val calendar2 =
                                                    Calendar.getInstance().apply { time = timestampDate }

                                                calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                                                        calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                                                        calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(
                                                    Calendar.DAY_OF_MONTH
                                                )
                                            }
                                        }.map { formatDateWithoutTime(it) }
                                    }
                                }
                            }
                        }
                    }
                }
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
                    getTaskListFlowUseCase(
                        dateStart = dayStartFinishTimestampPair.first,
                        dateFinish = dayStartFinishTimestampPair.second
                    ).collectLatest { selectedDayTaskList ->
                        taskListFlowMutable.update {
                            TaskMapper.domainModelListToUiModelList(selectedDayTaskList)
                        }
                    }
                }
            } else {
                //taskList of whole month
//                pagerStateCurrentPageFlowMutable.collectLatest { currentMonthIndex ->
//                getMonthListFlow().collectLatest { monthList ->
//                    monthList[currentMonthIndex].currentMonthDateList.let { currentMonthDateList ->
//                        val monthStartTimestamp =
//                            getDayStartFinishTimestampPair(currentMonthDateList.first())?.first
//                        val monthFinishTimestamp =
//                            getDayStartFinishTimestampPair(currentMonthDateList.last())?.second

                pagerStateCurrentPageFlowMutable.collectLatest { currentMonthIndex ->
                    getMonthListFlow().collectLatest { monthList ->
                        monthList[currentMonthIndex].currentMonthDateList.let { currentMonthDateList ->
                            val monthStartTimestamp =
                                getDayStartFinishTimestampPair(currentMonthDateList.first())?.first
                            val monthFinishTimestamp =
                                getDayStartFinishTimestampPair(currentMonthDateList.last())?.second

                            if (monthStartTimestamp != null && monthFinishTimestamp != null) {
                                getTaskListFlowUseCase(
                                    dateStart = monthStartTimestamp,
                                    dateFinish = monthFinishTimestamp
                                ).collectLatest { wholeMonthTaskList ->
                                    taskListFlowMutable.update {
                                        TaskMapper.domainModelListToUiModelList(wholeMonthTaskList)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
