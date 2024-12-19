package com.kassaev.planner.screen.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kassaev.planner.R
import com.kassaev.planner.data.entity.Task
import com.kassaev.planner.model.Month
import com.kassaev.planner.util.getCurrentDay
import com.kassaev.planner.util.getDay
import com.kassaev.planner.util.getMonthResourceId
import com.kassaev.planner.util.getYear
import com.kassaev.planner.util.isToday
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KFunction0

class CalendarMainScreenFragment : Fragment() {

    private val viewModel by viewModel<CalendarViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
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
        }
    }
}

@Composable
fun CalendarPager(
    pagerState: PagerState,
    monthList: List<Month>,
    currentMonthIndex: Int,
    selectedDate: String?,
    setSelectedDate: (String?) -> Unit,
    taskList: List<Task>,
    onAddTask: KFunction0<Unit>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(currentMonthIndex) {
        pagerState.scrollToPage(currentMonthIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        setSelectedDate(null)
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
                        if (selectedDate != null) {
                            onAddTask()
                        } else {
                            Toast.makeText(context, "Выберете дату", Toast.LENGTH_SHORT).show()
                        }
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                monthList[page].previousMonthLastWeekDateList.forEachIndexed { index, date ->
                    item {
                        CalendarGridItem(
                            index = index,
                            date = date,
                            selectedDate = selectedDate,
                            setSelectedDate = setSelectedDate
                        )
                    }
                }
                monthList[page].currentMonthDateList.forEachIndexed { index, date ->
                    item {
                        CalendarGridItem(
                            index = index + indexOffset,
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
                            index = index,
                            date = date,
                            selectedDate = selectedDate,
                            setSelectedDate = setSelectedDate
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                taskList.forEach { task ->
                    item {
                        Text(
                            text = "${task.name}-${task.description}"
                        )
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
    index: Int = 0,
    isCurrent: Boolean = false,
) {
    val context = LocalContext.current
    val columnIndex = index % 7
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
