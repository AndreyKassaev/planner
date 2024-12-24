package com.kassaev.planner.util

import com.kassaev.planner.domain.model.Month as MonthDomain
import com.kassaev.planner.model.Month as MonthUi

object MonthMapper {

    fun domainModelToUiModel(monthDomain: MonthDomain): MonthUi =
        MonthUi(
            previousMonthLastWeekDateList = monthDomain.previousMonthLastWeekDateList,
            currentMonthDateList = monthDomain.currentMonthDateList,
            followingMonthFirstWeekDateList = monthDomain.followingMonthFirstWeekDateList
        )

    fun uiModelToDomainModel(monthUi: MonthUi): MonthDomain =
        MonthDomain(
            previousMonthLastWeekDateList = monthUi.previousMonthLastWeekDateList,
            currentMonthDateList = monthUi.currentMonthDateList,
            followingMonthFirstWeekDateList = monthUi.followingMonthFirstWeekDateList
        )

    fun uiModelListToDomainModelList(monthUiList: List<MonthUi>): List<MonthDomain> =
        monthUiList.map { uiModelToDomainModel(it) }

    fun domainModelListToUiModelList(monthDomainList: List<MonthDomain>): List<MonthUi> =
        monthDomainList.map { domainModelToUiModel(it) }
}