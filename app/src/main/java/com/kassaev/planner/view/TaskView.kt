package com.kassaev.planner.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.kassaev.planner.databinding.TaskBinding

class TaskView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val binding = TaskBinding.inflate(LayoutInflater.from(context), this, true)

    fun setTime(time: String) {
        binding.taskTime.text = time
    }

    fun setName(name: String) {
        binding.taskName.text = name
    }

    fun setDescription(description: String) {
        binding.taskDescription.text = description
    }
}