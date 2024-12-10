package com.kassaev.planner.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kassaev.planner.databinding.FragmentCalendarMonthComponentBinding
import com.kassaev.planner.screen.calendar.CalendarViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarMonthComponentFragment : Fragment() {

    private lateinit var binding: FragmentCalendarMonthComponentBinding
    private val viewModel: CalendarViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarMonthComponentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initDb()
    }
}