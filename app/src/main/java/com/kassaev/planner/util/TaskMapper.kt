package com.kassaev.planner.util

import com.kassaev.planner.domain.model.Task as TaskDomainModel
import com.kassaev.planner.model.Task as TaskUiModel


object TaskMapper {

    fun domainModelToUiModel(taskDomainModel: TaskDomainModel): TaskUiModel =
        TaskUiModel(
            id = taskDomainModel.id,
            dateStart = taskDomainModel.dateStart,
            dateFinish = taskDomainModel.dateFinish,
            name = taskDomainModel.name,
            description = taskDomainModel.description
        )

    fun uiModelToDomainModel(taskUiModel: TaskUiModel): TaskDomainModel =
        TaskDomainModel(
            id = taskUiModel.id,
            dateStart = taskUiModel.dateStart,
            dateFinish = taskUiModel.dateFinish,
            name = taskUiModel.name,
            description = taskUiModel.description
        )

    fun domainModelListToUiModelList(taskDomainModelList: List<TaskDomainModel>): List<TaskUiModel> =
        taskDomainModelList.map { domainModelToUiModel(it) }

    fun uiModelListToDomainList(taskUiModelList: List<TaskUiModel>): List<TaskDomainModel> =
        taskUiModelList.map { uiModelToDomainModel(it) }
}