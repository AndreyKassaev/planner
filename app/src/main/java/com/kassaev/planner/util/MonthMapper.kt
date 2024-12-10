package com.kassaev.planner.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date
import com.kassaev.planner.data.entity.Month as MonthEntity
import com.kassaev.planner.model.Month as MonthModel

object MonthMapper {

    fun stringToDate(stringDate: String): Date {
        TODO()
    }

    fun dateToString(date: Date): String {
        TODO()
    }

    fun entityToModel(entity: MonthEntity): MonthModel =
        MonthModel(
            previousMonthLastWeekDateList = Json.decodeFromString<MonthModel>(
                entity.data
            ).previousMonthLastWeekDateList,
            currentMonthDateList = Json.decodeFromString<MonthModel>(
                entity.data
            ).currentMonthDateList,
            followingMonthFirstWeekDateList = Json.decodeFromString<MonthModel>(
                entity.data
            ).followingMonthFirstWeekDateList
        )

    fun modelToEntity(model: MonthModel): MonthEntity =
        MonthEntity(
            firstDay = model.currentMonthDateList.first().date,
            data = Json.encodeToString(model)
        )

    fun entityListToModelList(entityList: List<MonthEntity>): List<MonthModel> =
        entityList.map { entityToModel(it) }

    fun modelListToEntityList(modelList: List<MonthModel>): List<MonthEntity> =
        modelList.map { modelToEntity(it) }
}