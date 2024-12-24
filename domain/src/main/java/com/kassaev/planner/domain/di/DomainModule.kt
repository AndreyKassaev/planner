package com.kassaev.planner.domain.di

import com.kassaev.planner.domain.usecase.GetTaskByIdFlowUseCase
import com.kassaev.planner.domain.usecase.GetTaskByIdFlowUseCaseImpl
import com.kassaev.planner.domain.usecase.UpsertTaskUseCase
import com.kassaev.planner.domain.usecase.UpsertTaskUseCaseImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GetTaskByIdFlowUseCaseImpl) bind GetTaskByIdFlowUseCase::class
    singleOf(::UpsertTaskUseCaseImpl) bind UpsertTaskUseCase::class
}