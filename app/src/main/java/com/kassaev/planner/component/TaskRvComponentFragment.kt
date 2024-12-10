package com.kassaev.planner.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kassaev.planner.adapter.TaskAdapter
import com.kassaev.planner.databinding.FragmentTaskRvComponentBinding
import com.kassaev.planner.model.Task
import com.kassaev.planner.model.TaskRvItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
            adapter = TaskAdapter(MockSchedule.getTaskRvList())
        }
        MockSchedule.getTaskRvList()
            .forEach { hour ->
                println("${hour.hour} - ${hour.taskList.size}")
            }
    }
}

object MockSchedule {

    private val mockTaskList = listOf(
        Task(
            id = "1",
            dateStart = "1733743989960",
            dateFinish = "2",
            name = "singleTask",
            description = "desc"
        )
    )


    fun getTaskRvList(taskList: List<Task> = mockTaskList): List<TaskRvItem> {
        val hourList = MutableList(24) {
            TaskRvItem(
                hour = it.toString(),
                taskList = emptyList()
            )
        }

        taskList.forEach { task ->
            val index = timestampToLocalHours(task.dateStart).toInt()
            hourList[index] = hourList[index].copy(taskList = hourList[index].taskList + task)
        }
        return hourList
    }

    fun timestampToLocalHours(timestamp: String): String {
        val date = Date(timestamp.toLong())
        val sdf = SimpleDateFormat("HH", Locale("ru"))
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }
}