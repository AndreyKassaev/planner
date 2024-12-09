package com.kassaev.planner.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kassaev.planner.adapter.TaskAdapter
import com.kassaev.planner.databinding.FragmentTaskRvComponentBinding

class TaskRvComponentFragment : Fragment() {

    private lateinit var binding: FragmentTaskRvComponentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskRvComponentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.taskRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = TaskAdapter(emptyList())
        }
    }
}