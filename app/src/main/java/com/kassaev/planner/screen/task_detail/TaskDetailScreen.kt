package com.kassaev.planner.screen.task_detail

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kassaev.planner.navigation.LocalNavController
import com.kassaev.planner.util.formatDateWithDayAndMonth
import com.kassaev.planner.util.formatTime
import com.kassaev.planner.util.timestampToDate
import com.kassaev.planner.util.timestampToTimePair
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

@Composable
fun TaskDetailScreen(
    viewModel: TaskDetailViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val task by viewModel.getTaskFlow().collectAsStateWithLifecycle()
    val isTimeCorrect = task.dateStart <= task.dateFinish

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TaskDatePicker(
            onDateSelected = viewModel::setDate,
            date = timestampToDate(task.dateStart)
        )
        TaskTimePicker(
            setTimeStart = viewModel::setTimeStart,
            setTimeFinish = viewModel::setTimeFinish,
            dateStart = task.dateStart,
            dateFinish = task.dateFinish
        )
        TaskName(
            taskName = task.name,
            setTaskName = viewModel::setTaskName
        )
        TaskDescription(
            taskDescription = task.description,
            setTaskDescription = viewModel::setTaskDescription
        )
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                if (isTimeCorrect) {
                    viewModel.saveTask()
                    navController.popBackStack()
                } else {
                    Toast.makeText(
                        context,
                        "Время окончания должно быть позже времени начала",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        ) {
            Text("Сохранить")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDatePicker(
    modifier: Modifier = Modifier
        .fillMaxSize(),
    onDateSelected: (Long) -> Unit,
    date: Date
) {
    val offset = TimeZone.getDefault().getOffset(date.time)
    val dateWithOffsetTimestamp by remember { mutableStateOf(date.time + offset) }
    val datePickerState = rememberDatePickerState()
    var isDatePickerDialogOpen by remember {
        mutableStateOf(false)
    }

    Text(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                isDatePickerDialogOpen = true
            },
        text = formatDateWithDayAndMonth(date),
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    )
    if (isDatePickerDialogOpen) {
        DatePickerDialog(
            onDismissRequest = { isDatePickerDialogOpen = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDate ->
                            onDateSelected(selectedDate)
                        }
                        isDatePickerDialogOpen = false
                    }
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isDatePickerDialogOpen = false
                    }
                ) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(
                showModeToggle = false,
                state = datePickerState,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTimePicker(
    modifier: Modifier = Modifier,
    setTimeStart: (Pair<Int, Int>) -> Unit,
    setTimeFinish: (Pair<Int, Int>) -> Unit,
    dateStart: Long,
    dateFinish: Long,

    ) {
    val currentTime = Calendar.getInstance()
    var isStartTimePickerOpen by remember {
        mutableStateOf(false)
    }
    var isFinishTimePickerOpen by remember {
        mutableStateOf(false)
    }
    val startTimePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    val finishTimePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )
    LaunchedEffect(dateStart) {
        val timePair = timestampToTimePair(dateStart)
        startTimePickerState.apply {
            hour = timePair.first
            minute = timePair.second
        }
    }
    LaunchedEffect(dateFinish) {
        val timePair = timestampToTimePair(dateFinish)
        finishTimePickerState.apply {
            hour = timePair.first
            minute = timePair.second
        }
    }
    if (isStartTimePickerOpen) {
        TimePickerDialog(
            timePickerState = startTimePickerState,
            onDismissRequest = {
                isStartTimePickerOpen = false
            },
            onSaveRequest = setTimeStart
        )
    }
    if (isFinishTimePickerOpen) {
        TimePickerDialog(
            timePickerState = finishTimePickerState,
            onDismissRequest = {
                isFinishTimePickerOpen = false
            },
            onSaveRequest = setTimeFinish
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                .padding(8.dp)
                .clickable {
                    isStartTimePickerOpen = true
                },
            text = "${formatTime(startTimePickerState.hour)} : ${formatTime(startTimePickerState.minute)}",
            fontSize = 24.sp
        )
        Text(
            textAlign = TextAlign.Center,
            text = " - ",
            fontSize = 24.sp
        )
        Text(
            modifier = Modifier
                .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                .padding(8.dp)
                .clickable {
                    isFinishTimePickerOpen = true
                },
            text = "${formatTime(finishTimePickerState.hour)} : ${
                formatTime(
                    finishTimePickerState.minute
                )
            }",
            fontSize = 24.sp
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onSaveRequest: (Pair<Int, Int>) -> Unit,
    timePickerState: TimePickerState
) {
    Dialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(
                    state = timePickerState,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        modifier = Modifier
                            .alpha(0.5F),
                        onClick = {
                            onDismissRequest()
                        },
                    ) {
                        Text("Отмена")
                    }
                    Button(
                        onClick = {
                            onSaveRequest(Pair(timePickerState.hour, timePickerState.minute))
                            onDismissRequest()
                        }
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}

@Composable
fun TaskName(
    taskName: String,
    setTaskName: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = taskName,
        onValueChange = { setTaskName(it) },
        label = { Text("Название") }
    )
}

@Composable
fun TaskDescription(
    modifier: Modifier = Modifier,
    taskDescription: String,
    setTaskDescription: (String) -> Unit
) {

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = taskDescription,
        onValueChange = { setTaskDescription(it) },
        label = { Text("Описание") }
    )
}