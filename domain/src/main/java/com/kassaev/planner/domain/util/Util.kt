package com.kassaev.planner.domain.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Util {

    fun dateStringToDate(dateString: String): Date {

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
        val calendar = Calendar.getInstance()
        calendar.time = date

        return calendar.time
    }
}