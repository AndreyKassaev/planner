package com.kassaev.planner.screen.task_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kassaev.planner.navigation.LocalNavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun TaskDetailScreen(
    viewModel: TaskDetailViewModel = koinViewModel()
) {

    val navController = LocalNavController.current
    val task by viewModel.getTaskFlow().collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TaskDatePicker()
        TaskTimePicker()
        TaskName(
            taskName = task.name,
            setTaskName = viewModel::setTaskName
        )
        TaskDescription(
            taskDescription = task.description,
            setTaskDescription = viewModel::setTaskDescription
        )
        Button(
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

@Composable
fun TaskTimePicker(modifier: Modifier = Modifier) {

}

@Composable
fun TaskName(
    taskName: String,
    setTaskName: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    OutlinedTextField(
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
        value = taskDescription,
        onValueChange = { setTaskDescription(it) },
        label = { Text("Описание") }
    )
}