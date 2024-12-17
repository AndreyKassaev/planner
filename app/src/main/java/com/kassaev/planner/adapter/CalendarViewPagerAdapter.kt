package com.kassaev.planner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.kassaev.planner.R
import com.kassaev.planner.databinding.FragmentCalendarMonthComponentBinding
import com.kassaev.planner.model.CalendarDate
import com.kassaev.planner.model.Month
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7), // 7 columns
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    month.previousMonthLastWeekDateList.forEach { date ->
                        item {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .alpha(0.5F),
                                text = getDay(date),
                                fontSize = 24.sp,
                            )
                        }
                    }
                    month.currentMonthDateList.forEach { date ->
                        item {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = getDay(date),
                                fontSize = 24.sp,
                            )
                        }
                    }
                    month.followingMonthFirstWeekDateList.forEach { date ->
                        item {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .alpha(0.5F),
                                text = getDay(date),
                                fontSize = 24.sp,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getMonthResourceId(month: Month) =
        when (month.currentMonthDateList.first().date.split("-")[1].toInt()) {
            1 -> R.string.january
            2 -> R.string.february
            3 -> R.string.march
            4 -> R.string.april
            5 -> R.string.may
            6 -> R.string.june
            7 -> R.string.july
            8 -> R.string.august
            9 -> R.string.september
            10 -> R.string.october
            11 -> R.string.november
            12 -> R.string.december
            else -> R.string.unknown
        }


    private fun getYear(month: Month) =
        month.currentMonthDateList.first().date.split("-").first()

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

    private fun getCalendarDateTextView(
        date: CalendarDate,
        isCurrent: Boolean = false,
        context: Context
    ): TextView {
        val currentDate = Calendar.getInstance().time
        val textView = TextView(context)
        textView.setPadding(32, 32, 32, 32)
        textView.textSize = 24F
        textView.alpha = if (isCurrent) 1F else 0.25F
        textView.text = getDay(date)
        textView.setOnClickListener {
            println(date.date)
        }
        if (formatDateWithoutTime(SimpleDateFormat("yyyy-MM-dd").parse(date.date)) == formatDateWithoutTime(
                currentDate
            )
        ) textView.setTextColor(
            ContextCompat.getColor(context, R.color.calendar)
        )
        return textView
    }

    private fun getDay(date: CalendarDate) = date.date.split("-").last().toInt().toString()

    private fun formatDateWithoutTime(date: Date) =
        SimpleDateFormat("dd-MM-yyyy", Locale("ru")).format(date)

}