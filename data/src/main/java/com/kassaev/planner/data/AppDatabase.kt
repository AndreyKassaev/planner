package com.kassaev.planner.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kassaev.planner.data.dao.MonthDao
import com.kassaev.planner.data.dao.TaskDao
import com.kassaev.planner.data.entity.Month
import com.kassaev.planner.data.entity.Task

@Database(
    entities = [
        Month::class,
        Task::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun monthDao(): MonthDao
    abstract fun taskDao(): TaskDao
}