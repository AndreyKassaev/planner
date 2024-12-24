package com.kassaev.planner.di

import android.content.Context
import androidx.room.Room
import com.kassaev.planner.data.AppDatabase
import com.kassaev.planner.data.dao.MonthDao
import com.kassaev.planner.data.dao.TaskDao
import com.kassaev.planner.screen.calendar.CalendarViewModel
import com.kassaev.planner.screen.task_detail.TaskDetailViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidApplication() as Context,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    single<MonthDao> {
        get<AppDatabase>().monthDao()
    }

    single<TaskDao> {
        get<AppDatabase>().taskDao()
    }

    viewModelOf(::CalendarViewModel)

    viewModelOf(::TaskDetailViewModel)
}