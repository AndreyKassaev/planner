package com.kassaev.planner.di

import android.content.Context
import androidx.room.Room
import com.kassaev.planner.data.AppDatabase
import com.kassaev.planner.data.dao.MonthDao
import com.kassaev.planner.data.repository.CalendarRepository
import com.kassaev.planner.data.repository.CalendarRepositoryImpl
import com.kassaev.planner.screen.calendar.CalendarViewModel
import com.kassaev.planner.screen.task_detail.TaskDetailViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
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
        val database = get<AppDatabase>()
        database.userDao()
    }

    singleOf(::CalendarRepositoryImpl) bind CalendarRepository::class

    viewModelOf(::CalendarViewModel)

    viewModelOf(::TaskDetailViewModel)
}