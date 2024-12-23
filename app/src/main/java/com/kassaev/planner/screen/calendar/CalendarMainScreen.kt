package com.kassaev.planner.screen.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kassaev.planner.R
import com.kassaev.planner.model.Month
import com.kassaev.planner.model.Task
import com.kassaev.planner.navigation.LocalNavController
import com.kassaev.planner.navigation.TaskDetail
import com.kassaev.planner.util.formatTime
import com.kassaev.planner.util.getCurrentDay
import com.kassaev.planner.util.getDay
import com.kassaev.planner.util.getDayOfMonth
import com.kassaev.planner.util.getMonthResourceId
import com.kassaev.planner.util.getYear
import com.kassaev.planner.util.isToday
import com.kassaev.planner.util.timestampToDate
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalendarManScreen(
    viewModel: CalendarViewModel = koinViewModel()
) {
    val monthList by viewModel.getMonthListFlow()
        .collectAsStateWithLifecycle(emptyList())
    val currentMonthIndex by viewModel.getCurrentMonthIndexFlow()
        .collectAsStateWithLifecycle()
    val selectedDate by viewModel.getSelectedDateFlow()
        .collectAsStateWithLifecycle()
    val taskList by viewModel.getTaskListFlow()
        .collectAsStateWithLifecycle()
    val navController = LocalNavController.current

    CalendarPager(
        monthList = monthList,
        currentMonthIndex = currentMonthIndex,
        selectedDate = selectedDate,
        setSelectedDate = viewModel::setSelectedDate,
        taskList = taskList,
        navController = navController,
        setPagerStateCurrentPage = viewModel::setPagerStateCurrentPage
    )
}

@Composable
fun CalendarPager(
    monthList: List<Month>,
    currentMonthIndex: Int,
    selectedDate: String?,
    setSelectedDate: (String?) -> Unit,
    taskList: List<Task>,
    navController: NavController,
    setPagerStateCurrentPage: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(
        pageCount = {
            monthList.size
        }
    )
    val scope = rememberCoroutineScope()
    val dayOfWeekList = listOf(
        stringResource(R.string.monday_short),
        stringResource(R.string.tuesday_short),
        stringResource(R.string.wednesday_short),
        stringResource(R.string.thursday_short),
        stringResource(R.string.friday_short),
        stringResource(R.string.saturday_short),
        stringResource(R.string.sunday_short),
    )
    LaunchedEffect(pagerState.currentPage) {
        setPagerStateCurrentPage(pagerState.currentPage)
        setSelectedDate(null)
    }
    LaunchedEffect(currentMonthIndex) {
        pagerState.scrollToPage(currentMonthIndex)
    }
    LaunchedEffect(monthList.size) {
        if (pagerState.currentPage != currentMonthIndex) {
            pagerState.scrollToPage(currentMonthIndex)
        }
    }
    HorizontalPager(
        state = pagerState
    ) { page ->
        val indexOffset = monthList[page].previousMonthLastWeekDateList.size
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(
                            TaskDetail(
                                selectedDate = selectedDate
                            )
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_task),
                        contentDescription = null
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(getYear(monthList[page]))
                    Text(
                        stringResource(
                            getMonthResourceId(monthList[page])
                        )
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                setSelectedDate(null)
                                pagerState.scrollToPage(currentMonthIndex)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.calendar_current_date),
                            contentDescription = null
                        )
                    }
                    Text(
                        modifier = Modifier
                            .offset(0.dp, 3.dp),
                        text = getCurrentDay()
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                dayOfWeekList.forEach { dayOfWeek ->
                    Text(
                        text = dayOfWeek
                    )
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                monthList[page].previousMonthLastWeekDateList.forEachIndexed { index, date ->
                    item {
                        CalendarGridItem(
                            dayIndexInMonth = index,
                            date = date,
                            selectedDate = selectedDate,
                            setSelectedDate = setSelectedDate
                        )
                    }
                }
                monthList[page].currentMonthDateList.forEachIndexed { index, date ->
                    item {
                        CalendarGridItem(
                            dayIndexInMonth = index + indexOffset,
                            date = date,
                            selectedDate = selectedDate,
                            setSelectedDate = setSelectedDate,
                            isCurrent = true
                        )
                    }
                }
                monthList[page].followingMonthFirstWeekDateList.forEachIndexed { index, date ->
                    item {
                        CalendarGridItem(
                            dayIndexInMonth = index,
                            date = date,
                            selectedDate = selectedDate,
                            setSelectedDate = setSelectedDate
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                taskList.forEach { task ->
                    val dateStart = timestampToDate(task.dateStart)
                    item {
                        Row(
                            modifier = Modifier
                                .border(
                                    BorderStroke(1.dp, Color.LightGray),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(8.dp)
                                .clickable {
                                    navController.navigate(
                                        TaskDetail(
                                            taskId = task.id
                                        )
                                    )
                                }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${getDayOfMonth(dateStart)}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${formatTime(dateStart.hours)}:${formatTime(dateStart.minutes)}"
                                )
                            }
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                text = task.name
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarGridItem(
    date: String,
    selectedDate: String?,
    setSelectedDate: (String) -> Unit,
    dayIndexInMonth: Int = 0,
    isCurrent: Boolean = false,
) {
    val context = LocalContext.current
    val columnIndex = dayIndexInMonth % 7
    val backgroundColor = when (columnIndex) {
        5, 6 -> Color(ContextCompat.getColor(context, R.color.highlightColor))
        else -> Color.Transparent
    }
    Text(
        modifier = Modifier
            .padding(8.dp)
            .background(color = if (isCurrent) backgroundColor else Color.Transparent)
            .alpha(if (isCurrent) 1F else 0.5F)
            .then(
                if (isToday(date)) {
                    Modifier.border(
                        BorderStroke(2.dp, Color.Blue),
                        shape = RoundedCornerShape(4.dp)
                    )
                } else {
                    Modifier
                }
            )
            .then(
                if (selectedDate == date) {
                    Modifier
                        .background(Color.Gray)
                } else {
                    Modifier
                }
            )
            .clickable {
                setSelectedDate(date)
            },
        text = getDay(date),
        fontSize = 24.sp,
        textAlign = TextAlign.Center
    )
}