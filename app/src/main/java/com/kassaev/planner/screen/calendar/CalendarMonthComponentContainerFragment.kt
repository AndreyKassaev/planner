package com.kassaev.planner.screen.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kassaev.planner.adapter.GridItem
import com.kassaev.planner.databinding.FragmentCalendarMonthComponentContainerBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    fun getCurrentMonth() {

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