package com.kassaev.planner.data.di

import com.kassaev.planner.data.repository.CalendarRepositoryImpl
import com.kassaev.planner.domain.repository.CalendarRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {

    singleOf(::CalendarRepositoryImpl) bind CalendarRepository::class
}