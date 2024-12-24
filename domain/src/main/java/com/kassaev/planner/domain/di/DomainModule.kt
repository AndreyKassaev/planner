package com.kassaev.planner.domain.di

import com.kassaev.planner.domain.usecase.GetTaskByIdUseCase
import com.kassaev.planner.domain.usecase.GetTaskByIdUseCaseImpl
import com.kassaev.planner.domain.usecase.UpsertTaskUseCase
import com.kassaev.planner.domain.usecase.UpsertTaskUseCaseImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GetTaskByIdUseCaseImpl) bind GetTaskByIdUseCase::class
    singleOf(::UpsertTaskUseCaseImpl) bind UpsertTaskUseCase::class
}