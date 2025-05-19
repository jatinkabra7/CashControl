package com.jk.cashcontrol.presentation.statistics

data class StatisticsState(
    val todayIncome : Float = 0f,
    val todayExpense : Float = 0f,
    val thisMonthIncome : Float = 0f,
    val thisMonthExpense : Float = 0f,
    val thisYearIncome : Float = 0f,
    val thisYearExpense : Float = 0f,
    val isGenerateSummaryButtonClicked : Boolean = false,
    val todayTopIncomeCategory: String = "Other",
    val todayTopExpenseCategory: String = "Other",
    val thisMonthTopIncomeCategory: String = "Other",
    val thisMonthTopExpenseCategory: String = "Other",
    val thisYearIncomeCategory: String = "Other",
    val thisYearExpenseCategory: String = "Other",
)
