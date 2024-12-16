package com.kassaev.planner.screen.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kassaev.planner.adapter.CalendarViewPagerAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getCalendarFlow().collect { monthList ->
                binding.calendarViewPager.adapter = CalendarViewPagerAdapter(items = monthList)
            }
        }
    }
}