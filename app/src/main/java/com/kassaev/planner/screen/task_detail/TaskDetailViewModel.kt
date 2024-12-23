package com.kassaev.planner.screen.task_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kassaev.planner.data.repository.CalendarRepository
import com.kassaev.planner.model.Task
import com.kassaev.planner.navigation.TaskDetail
import com.kassaev.planner.util.TaskTime
import com.kassaev.planner.util.dateStringToDate
import com.kassaev.planner.util.timestampToDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class TaskDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    private val taskFlowMutable = MutableStateFlow(Task.mock)
    private val taskFlow: StateFlow<Task> = taskFlowMutable

    private val taskTimeFlowMutable = MutableStateFlow(TaskTime.mock)

    init {
        viewModelScope.launch {
            val taskDetail = savedStateHandle.toRoute<TaskDetail>()
            taskDetail.taskId?.let { taskId ->
                calendarRepository.getTaskByIdFlow(id = taskId).collectLatest { task ->
                    taskFlowMutable.update {
                        task
                    }
                }
            }
            taskDetail.selectedDate?.let { selectedDate ->
                taskFlowMutable.update { task ->
                    task.copy(
                        dateStart = dateStringToDate(selectedDate).time
                    )
                }
            }
        }
    }

    fun getTaskFlow() = taskFlow

    fun setTaskName(name: String) {
        viewModelScope.launch {
            taskFlowMutable.update { task ->
                task.copy(
                    name = name
                )
            }
        }
    }

    fun setTaskDescription(description: String) {
        viewModelScope.launch {
            taskFlowMutable.update { task ->
                task.copy(
                    description = description
                )
            }
        }
    }

    fun saveTask() {
        viewModelScope.launch {
            calendarRepository.upsertTask(
                task = taskFlow.first()
            )
        }
    }

    fun setTimeStart(hourMinutePair: Pair<Int, Int>) {
        viewModelScope.launch {
            taskTimeFlowMutable.update { taskTime ->
                taskTime.copy(
                    start = hourMinutePair
                )
            }
        }
    }

    fun setTimeFinish(hourMinutePair: Pair<Int, Int>) {
        viewModelScope.launch {
            taskTimeFlowMutable.update { taskTime ->
                taskTime.copy(
                    finish = hourMinutePair
                )
            }
        }
    }

    fun setDate(timestamp: Long) {
        val date = timestampToDate(timestamp)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp

        viewModelScope.launch {
            taskTimeFlowMutable.collectLatest { taskTime ->
                calendar.set(Calendar.HOUR_OF_DAY, taskTime.start.first ?: 0)
                calendar.set(Calendar.MINUTE, taskTime.start.second ?: 0)
                val dateStart = calendar.timeInMillis

                calendar.set(Calendar.HOUR_OF_DAY, taskTime.finish.first ?: 0)
                calendar.set(Calendar.MINUTE, taskTime.finish.second ?: 0)
                val dateFinish = calendar.timeInMillis

                taskFlowMutable.update { task ->
                    task.copy(
                        dateStart = dateStart,
                        dateFinish = dateFinish
                    )
                }
            }
        }
    }
}