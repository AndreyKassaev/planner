package com.kassaev.planner.screen.task_detail

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kassaev.planner.navigation.LocalNavController
import com.kassaev.planner.util.formatNumber
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@Composable
fun TaskDetailScreen(
    viewModel: TaskDetailViewModel = koinViewModel()
) {

    val navController = LocalNavController.current
    val task by viewModel.getTaskFlow().collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TaskDatePicker()
        TaskTimePicker(
            setTimeStart = {},
            setTimeFinish = {}
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
                viewModel.saveTask()
                navController.popBackStack()
            }
        ) {
            Text("Сохранить")
        }
    }
}

@Composable
fun TaskDatePicker(modifier: Modifier = Modifier) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTimePicker(
    modifier: Modifier = Modifier,
    setTimeStart: (Long) -> Unit,
    setTimeFinish: (Long) -> Unit,
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

    if (isStartTimePickerOpen) {
        TimePickerDialog(
            timePickerState = startTimePickerState,
            onDismissRequest = {
                isStartTimePickerOpen = false
            }
        )
    }
    if (isFinishTimePickerOpen) {
        TimePickerDialog(
            timePickerState = finishTimePickerState,
            onDismissRequest = {
                isFinishTimePickerOpen = false
            }
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
            text = "${formatNumber(startTimePickerState.hour)} : ${formatNumber(startTimePickerState.minute)}",
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
            text = "${formatNumber(finishTimePickerState.hour)} : ${
                formatNumber(
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