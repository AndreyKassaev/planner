package com.kassaev.planner.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kassaev.planner.databinding.FragmentCalendarMonthComponentBinding
import com.kassaev.planner.model.Month

class CalendarViewPagerAdapter(
    private val items: List<Month>
) : RecyclerView.Adapter<CalendarViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: FragmentCalendarMonthComponentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(month: Month) {
            month.previousMonthLastWeekDateList.forEach { date ->
//                binding.calendarGridCellContainer.addView(
//                    getCalendarDateTextView(date)
//                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

//    private fun getCalendarDateTextView(date: Date, isCurrent: Boolean = false): TextView {
//        val currentDate = Calendar.getInstance().time
//        val textView = TextView(requireContext())
//        textView.setPadding(32, 32, 32, 32)
//        textView.textSize = 24F
//        textView.alpha = if(isCurrent) 1F else 0.25F
//        textView.text = date.date.toString()
//        if (formatDateWithoutTime(date) == formatDateWithoutTime(currentDate)) textView.setTextColor(
//            ContextCompat.getColor(requireContext(), R.color.calendar))
//        return textView
//    }
//
//    private fun formatDateWithoutTime(date: Date) =
//        SimpleDateFormat("dd-MM-yyyy", Locale("ru")).format(date)

}