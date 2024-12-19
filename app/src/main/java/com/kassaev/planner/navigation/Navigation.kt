package com.kassaev.planner.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kassaev.planner.screen.calendar.CalendarManScreen

@Composable
fun Navigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    Scaffold(
        floatingActionButton = {
            AddTaskFAB()
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .padding(paddingValues),
            navController = navController,
            startDestination = "calendar"
        ) {
            composable(
                route = "calendar"
            ) {
                CalendarManScreen()
            }
        }
    }
}

@Composable
fun AddTaskFAB() {

}
