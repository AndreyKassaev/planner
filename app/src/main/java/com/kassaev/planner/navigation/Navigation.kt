package com.kassaev.planner.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kassaev.planner.screen.calendar.CalendarManScreen
import com.kassaev.planner.screen.task_detail.TaskDetailScreen

val LocalNavController = compositionLocalOf<NavController> {
    error("No NavController found!")
}

@Composable
fun Navigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        Scaffold(
            floatingActionButton = {
                AddTaskFAB()
            }
        ) { paddingValues ->
            NavHost(
                modifier = Modifier
                    .padding(paddingValues),
                navController = navController,
                startDestination = CalendarMainScreen
            ) {
                composable<CalendarMainScreen> {
                    CalendarManScreen()
                }

                composable<TaskDetail> {
                    TaskDetailScreen()
                }
            }
        }
    }
}

@Composable
fun AddTaskFAB() {

}
