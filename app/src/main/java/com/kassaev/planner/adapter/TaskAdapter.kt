package com.kassaev.planner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kassaev.planner.databinding.TaskRvItemBinding
import com.kassaev.planner.model.TaskRvItem
import com.kassaev.planner.view.TaskView

class TaskAdapter(
    private val items: List<TaskRvItem>
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: TaskRvItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(taskRvItem: TaskRvItem) {
            binding.taskRvItemContainer.removeAllViews()
            binding.taskRvItemHour.text = taskRvItem.hour
            taskRvItem.taskList.forEach { task ->

                val taskView = TaskView(itemView.context)
                taskView.setTime(task.dateStart)
                taskView.setName(task.name)
                taskView.setDescription(task.description)

                binding.taskRvItemContainer.addView(taskView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TaskRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

}