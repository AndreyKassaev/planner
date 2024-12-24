package com.kassaev.planner.domain.di

import com.kassaev.planner.domain.usecase.GetMonthIndexDeferredUseCase
import com.kassaev.planner.domain.usecase.GetMonthIndexDeferredUseCaseImpl
import com.kassaev.planner.domain.usecase.GetMonthListFlowUseCase
import com.kassaev.planner.domain.usecase.GetMonthListFlowUseCaseImpl
import com.kassaev.planner.domain.usecase.GetTaskByIdFlowUseCase
import com.kassaev.planner.domain.usecase.GetTaskByIdFlowUseCaseImpl
import com.kassaev.planner.domain.usecase.GetTaskListFlowUseCase
import com.kassaev.planner.domain.usecase.GetTaskListFlowUseCaseImpl
import com.kassaev.planner.domain.usecase.UpsertTaskUseCase
import com.kassaev.planner.domain.usecase.UpsertTaskUseCaseImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GetTaskByIdFlowUseCaseImpl) bind GetTaskByIdFlowUseCase::class
    singleOf(::UpsertTaskUseCaseImpl) bind UpsertTaskUseCase::class
    singleOf(::GetMonthListFlowUseCaseImpl) bind GetMonthListFlowUseCase::class
    singleOf(::GetMonthIndexDeferredUseCaseImpl) bind GetMonthIndexDeferredUseCase::class
    singleOf(::GetTaskListFlowUseCaseImpl) bind GetTaskListFlowUseCase::class
}