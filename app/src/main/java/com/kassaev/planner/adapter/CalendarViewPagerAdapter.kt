package com.kassaev.planner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.kassaev.planner.R
import com.kassaev.planner.databinding.FragmentCalendarMonthComponentBinding
import com.kassaev.planner.model.CalendarDate
import com.kassaev.planner.model.Month
import com.kassaev.planner.util.getDay
import com.kassaev.planner.util.getMonthResourceId
import com.kassaev.planner.util.getYear
import com.kassaev.planner.util.isToday

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
                    month.previousMonthLastWeekDateList.forEachIndexed { index, date ->
                        item {
                            GridItem(index = index, date = date, selectedDate =)
                        }
                    }
                    month.currentMonthDateList.forEachIndexed { index, date ->
                        item {
                            GridItem(index = index + indexOffset, date = date, isCurrent = true)
                        }
                    }
                    month.followingMonthFirstWeekDateList.forEachIndexed { index, date ->
                        item {
                            GridItem(index = index, date = date)
                        }
                    }
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

@Composable
fun GridItem(
    index: Int,
    date: CalendarDate,
    selectedDate: CalendarDate,
    setSelectedDate: (CalendarDate) -> Unit,
    isCurrent: Boolean = false,
) {
    val context = LocalContext.current
    val columnIndex = index % 7
    val backgroundColor = when (columnIndex) {
        5, 6 -> Color(ContextCompat.getColor(context, R.color.highlightColor))
        else -> Color.Transparent
    }
    Text(
        modifier = Modifier
            .padding(8.dp)
            .alpha(if (isCurrent) 1F else 0.5F)
            .background(color = if (isCurrent) backgroundColor else if (selectedDate == date) Color.Gray else Color.Transparent)
            .then(
                if (isToday(date)) {
                    Modifier.border(
                        BorderStroke(2.dp, Color.Blue),
                        shape = RoundedCornerShape(4.dp)
                    )
                } else {
                    Modifier
                }
            )
            .clickable {
                setSelectedDate(date)
            },
        text = getDay(date),
        fontSize = 24.sp,
        textAlign = TextAlign.Center
    )
}