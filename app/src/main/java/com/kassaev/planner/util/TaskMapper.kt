package com.kassaev.planner.util

import com.kassaev.planner.data.entity.Task as TaskEntity
import com.kassaev.planner.model.Task as TaskModel

object TaskMapper {

    fun entityToModel(entity: TaskEntity): TaskModel =
        TaskModel(
            id = entity.id,
            dateStart = entity.dateStart,
            dateFinish = entity.dateFinish,
            name = entity.name,
            description = entity.description
        )

    fun modelToEntity(model: TaskModel): TaskEntity =
        TaskEntity(
            id = model.id,
            dateStart = model.dateStart,
            dateFinish = model.dateFinish,
            name = model.name,
            description = model.description
        )

    fun entityListToModelList(entityList: List<TaskEntity>): List<TaskModel> =
        entityList.map { entityToModel(it) }

    fun modelListToEntityList(modelList: List<TaskModel>): List<TaskEntity> =
        modelList.map { modelToEntity(it) }
}