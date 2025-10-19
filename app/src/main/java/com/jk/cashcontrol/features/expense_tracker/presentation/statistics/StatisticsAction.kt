package com.jk.cashcontrol.features.expense_tracker.presentation.statistics

interface StatisticsAction {
    data class OnTodayGenerateSummaryClick(val state : TodayState) : StatisticsAction
    data class OnThisMonthGenerateSummaryClick(val state : ThisMonthState) : StatisticsAction
    data class OnThisYearGenerateSummaryClick(val state : ThisYearState) : StatisticsAction
    data object ReloadData : StatisticsAction
}