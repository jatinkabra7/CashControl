package com.jk.cashcontrol.presentation.statistics

interface StatisticsAction {
    data class OnTodayGenerateSummaryClick(val state : StatisticsState) : StatisticsAction
    data class OnThisMonthGenerateSummaryClick(val state : StatisticsState) : StatisticsAction
    data class OnThisYearGenerateSummaryClick(val state : StatisticsState) : StatisticsAction
    data object ReloadData : StatisticsAction
}