package com.kassaev.planner.screen.calendar

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalendarManScreen(
    viewModel: CalendarViewModel = koinViewModel()
) {
    val monthList by viewModel.getMonthListFlow()
        .collectAsStateWithLifecycle(emptyList())
    val currentMonthIndex by viewModel.getCurrentMonthIndexFlow()
        .collectAsStateWithLifecycle()
    val selectedDate by viewModel.getSelectedDateFlow().collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = {
        monthList.size
    })
    val taskList by viewModel.getTaskListFlow()
        .collectAsStateWithLifecycle()

    LaunchedEffect(pagerState.currentPage) {
        viewModel.setPagerStateCurrentPage(pagerState.currentPage)
    }
    CalendarPager(
        pagerState = pagerState,
        monthList = monthList,
        currentMonthIndex = currentMonthIndex,
        selectedDate = selectedDate,
        setSelectedDate = viewModel::setSelectedDate,
        taskList = taskList,
        onAddTask = viewModel::addMockTask
    )
}
