package com.kassaev.planner.data.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.kassaev.planner.data.entity.Month as MonthEntity
import com.kassaev.planner.data.model.Month as MonthDataModel
import com.kassaev.planner.domain.model.Month as MonthDomainModel

object MonthMapper {

    fun domainModelToDataModel(monthDomainModel: MonthDomainModel): MonthDataModel =
        MonthDataModel(
            previousMonthLastWeekDateList = monthDomainModel.previousMonthLastWeekDateList,
            currentMonthDateList = monthDomainModel.currentMonthDateList,
            followingMonthFirstWeekDateList = monthDomainModel.followingMonthFirstWeekDateList
        )

    fun dataModelToDomainModel(monthDataModel: MonthDataModel): MonthDomainModel =
        MonthDomainModel(
            previousMonthLastWeekDateList = monthDataModel.previousMonthLastWeekDateList,
            currentMonthDateList = monthDataModel.currentMonthDateList,
            followingMonthFirstWeekDateList = monthDataModel.followingMonthFirstWeekDateList
        )

    fun entityToDomainModel(entity: MonthEntity): MonthDomainModel =
        dataModelToDomainModel(entityToDataModel(entity))

    fun entityToDataModel(entity: MonthEntity): MonthDataModel =
        MonthDataModel(
            previousMonthLastWeekDateList = Json.decodeFromString<MonthDataModel>(
                entity.data
            ).previousMonthLastWeekDateList,
            currentMonthDateList = Json.decodeFromString<MonthDataModel>(
                entity.data
            ).currentMonthDateList,
            followingMonthFirstWeekDateList = Json.decodeFromString<MonthDataModel>(
                entity.data
            ).followingMonthFirstWeekDateList
        )

    fun dataModelToEntity(model: MonthDataModel): MonthEntity =
        MonthEntity(
            firstDay = model.currentMonthDateList.first(),
            data = Json.encodeToString(model)
        )

    fun domainModelListToDataModelList(monthDomainModelList: List<MonthDomainModel>): List<MonthDataModel> =
        monthDomainModelList.map { domainModelToDataModel(it) }

    fun dataModelListToDomainModelList(monthDataModelList: List<MonthDataModel>): List<MonthDomainModel> =
        monthDataModelList.map { dataModelToDomainModel(it) }

    fun entityListToDomainModelList(entityList: List<MonthEntity>): List<MonthDomainModel> =
        entityList.map { entityToDomainModel(it) }

    fun entityListToDataModelList(entityList: List<MonthEntity>): List<MonthDataModel> =
        entityList.map { entityToDataModel(it) }

    fun dataModelListToEntityList(modelList: List<MonthDataModel>): List<MonthEntity> =
        modelList.map { dataModelToEntity(it) }
}