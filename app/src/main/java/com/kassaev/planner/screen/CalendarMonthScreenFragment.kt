package com.kassaev.planner.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.kassaev.planner.R
import com.kassaev.planner.component.CalendarMonthComponentFragment
import com.kassaev.planner.component.TaskRvComponentFragment
import com.kassaev.planner.databinding.FragmentCalendarMonthScreenBinding

class CalendarMonthScreenFragment : Fragment() {

    private lateinit var binding: FragmentCalendarMonthScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarMonthScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.commit {
            replace(R.id.calendarContainer, CalendarMonthComponentFragment())
            replace(R.id.taskRvContainer, TaskRvComponentFragment())
        }

    }
}