package com.kassaev.planner.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kassaev.planner.R
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
        findNavController().navigate(R.id.taskDetailScreenFragment)
    }
}