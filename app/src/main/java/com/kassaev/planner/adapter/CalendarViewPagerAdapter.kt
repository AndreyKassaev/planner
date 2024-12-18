package com.kassaev.planner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.kassaev.planner.databinding.FragmentCalendarMonthComponentBinding
import com.kassaev.planner.model.Month
import com.kassaev.planner.util.getMonthResourceId
import com.kassaev.planner.util.getYear

class CalendarViewPagerAdapter(
    private val items: List<Month>
) : RecyclerView.Adapter<CalendarViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: FragmentCalendarMonthComponentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(month: Month) {
            binding.calendarYear.text = getYear(month)
            binding.calendarMonth.text = getString(binding.root.context, getMonthResourceId(month))
            binding.composeView.setContent {
                val indexOffset = month.previousMonthLastWeekDateList.size

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
//                    month.previousMonthLastWeekDateList.forEachIndexed { index, date ->
//                        item {
//                            GridItem(index = index, date = date, selectedDate =)
//                        }
//                    }
//                    month.currentMonthDateList.forEachIndexed { index, date ->
//                        item {
//                            GridItem(index = index + indexOffset, date = date, isCurrent = true)
//                        }
//                    }
//                    month.followingMonthFirstWeekDateList.forEachIndexed { index, date ->
//                        item {
//                            GridItem(index = index, date = date)
//                        }
//                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentCalendarMonthComponentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

