package com.kassaev.planner.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kassaev.planner.data.dao.MonthDao
import com.kassaev.planner.data.entity.Month

@Database(entities = [Month::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): MonthDao
}