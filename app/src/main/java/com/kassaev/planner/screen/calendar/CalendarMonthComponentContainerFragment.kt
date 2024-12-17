package com.kassaev.planner.screen.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kassaev.planner.R
import com.kassaev.planner.adapter.GridItem
import com.kassaev.planner.databinding.FragmentCalendarMonthComponentContainerBinding
import com.kassaev.planner.util.getMonthResourceId
import com.kassaev.planner.util.getYear
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

class CalendarMonthComponentContainerFragment : Fragment() {

    private lateinit var binding: FragmentCalendarMonthComponentContainerBinding
    private val viewModel: CalendarViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarMonthComponentContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getCalendarFlow().collect { monthList ->
                binding.calendarViewPager.setContent {
                    val pagerState = rememberPagerState(pageCount = {
                        monthList.size
                    })
                    val scope = rememberCoroutineScope()
                    LaunchedEffect(viewModel.getMonthRowNumberFlow()) {
                        viewModel.getMonthRowNumberFlow().collect { row_number ->
                            pagerState.scrollToPage(row_number - 1)
                        }
                    }
                    HorizontalPager(
                        state = pagerState
                    ) { page ->
                        val indexOffset = monthList[page].previousMonthLastWeekDateList.size
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column { }
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
                                                viewModel.getMonthRowNumberFlow()
                                                    .collect { row_number ->
                                                        pagerState.scrollToPage(row_number - 1)
                                                    }
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
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                monthList[page].previousMonthLastWeekDateList.forEachIndexed { index, date ->
                                    item {
                                        GridItem(index = index, date = date)
                                    }
                                }
                                monthList[page].currentMonthDateList.forEachIndexed { index, date ->
                                    item {
                                        GridItem(
                                            index = index + indexOffset,
                                            date = date,
                                            isCurrent = true
                                        )
                                    }
                                }
                                monthList[page].followingMonthFirstWeekDateList.forEachIndexed { index, date ->
                                    item {
                                        GridItem(index = index, date = date)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getCurrentDay(): String =
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()

}