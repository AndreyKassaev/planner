package com.kassaev.planner.data.util

import com.kassaev.planner.data.entity.Task as TaskEntity
import com.kassaev.planner.data.model.Task as TaskDataModel
import com.kassaev.planner.domain.model.Task as TaskDomainModel

object TaskMapper {

    fun dataModelToDomainModel(taskDataModel: TaskDataModel): TaskDomainModel =
        TaskDomainModel(
            id = taskDataModel.id,
            dateStart = taskDataModel.dateStart,
            dateFinish = taskDataModel.dateFinish,
            name = taskDataModel.name,
            description = taskDataModel.description
        )

    fun domainModelToDataModel(taskDomainModel: TaskDomainModel): TaskDataModel =
        TaskDataModel(
            id = taskDomainModel.id,
            dateStart = taskDomainModel.dateStart,
            dateFinish = taskDomainModel.dateFinish,
            name = taskDomainModel.name,
            description = taskDomainModel.description
        )

    fun entityToDataModel(entity: TaskEntity): TaskDataModel =
        TaskDataModel(
            id = entity.id,
            dateStart = entity.dateStart,
            dateFinish = entity.dateFinish,
            name = entity.name,
            description = entity.description
        )

    fun dataModelToEntity(model: TaskDataModel): TaskEntity =
        TaskEntity(
            id = model.id,
            dateStart = model.dateStart,
            dateFinish = model.dateFinish,
            name = model.name,
            description = model.description
        )

    fun domainModelToEntity(taskDomainModel: TaskDomainModel): TaskEntity =
        TaskEntity(
            id = taskDomainModel.id,
            dateStart = taskDomainModel.dateStart,
            dateFinish = taskDomainModel.dateFinish,
            name = taskDomainModel.name,
            description = taskDomainModel.description
        )

    fun entityToDomainModel(entity: TaskEntity): TaskDomainModel =
        dataModelToDomainModel(entityToDataModel(entity))

    fun entityListToDataModelList(entityList: List<TaskEntity>): List<TaskDataModel> =
        entityList.map { entityToDataModel(it) }

    fun entityListToDomainModelList(taskEntityList: List<TaskEntity>): List<TaskDomainModel> =
        taskEntityList.map { entityToDomainModel(it) }

    fun dataModelListToEntityList(modelList: List<TaskDataModel>): List<TaskEntity> =
        modelList.map { dataModelToEntity(it) }
}