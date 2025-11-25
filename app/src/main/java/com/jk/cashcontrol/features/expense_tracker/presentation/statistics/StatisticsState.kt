package com.jk.cashcontrol.features.expense_tracker.presentation.statistics

import com.jk.cashcontrol.features.expense_tracker.domain.model.CategoryWithAmount

data class StatisticsState(
    val todayIncome: Float = 0f,
    val todayExpense: Float = 0f,
    val thisMonthIncome: Float = 0f,
    val thisMonthExpense: Float = 0f,
    val thisYearIncome: Float = 0f,
    val thisYearExpense: Float = 0f,
    val todayTopIncomeCategory: String = "Other",
    val todayTopExpenseCategory: String = "Other",
    val thisMonthTopIncomeCategory: String = "Other",
    val thisMonthTopExpenseCategory: String = "Other",
    val thisYearTopIncomeCategory: String = "Other",
    val thisYearTopExpenseCategory: String = "Other",
    val isTodaySummaryGenerated: Boolean = false,
    val todayGeneratedSummary: String = "",
    val isThisMonthSummaryGenerated: Boolean = false,
    val thisMonthGeneratedSummary: String = "",
    val isThisYearSummaryGenerated: Boolean = false,
    val thisYearGeneratedSummary: String = "",
    val isTodaySummaryLoading: Boolean = false,
    val isThisMonthSummaryLoading: Boolean = false,
    val isThisYearSummaryLoading: Boolean = false,
    val todayExpenseCategories: List<CategoryWithAmount> = emptyList(),
    val todayIncomeCategories: List<CategoryWithAmount> = emptyList(),
    val thisMonthExpenseCategories: List<CategoryWithAmount> = emptyList(),
    val thisMonthIncomeCategories: List<CategoryWithAmount> = emptyList(),
    val thisYearExpenseCategories: List<CategoryWithAmount> = emptyList(),
    val thisYearIncomeCategories: List<CategoryWithAmount> = emptyList()
)

data class TodayState(
    val todayIncome: Float = 0f,
    val todayExpense: Float = 0f,
    val todayTopIncomeCategory: String = "Other",
    val todayTopExpenseCategory: String = "Other",
    val isTodaySummaryGenerated: Boolean = false,
    val todayGeneratedSummary: String = "",
    val isTodaySummaryLoading: Boolean = false,
    val todayExpenseCategories: List<CategoryWithAmount> = emptyList(),
    val todayIncomeCategories: List<CategoryWithAmount> = emptyList()
)

data class ThisMonthState(
    val thisMonthIncome: Float = 0f,
    val thisMonthExpense: Float = 0f,
    val thisMonthTopIncomeCategory: String = "Other",
    val thisMonthTopExpenseCategory: String = "Other",
    val isThisMonthSummaryGenerated: Boolean = false,
    val thisMonthGeneratedSummary: String = "",
    val isThisMonthSummaryLoading: Boolean = false,
    val thisMonthExpenseCategories: List<CategoryWithAmount> = emptyList(),
    val thisMonthIncomeCategories: List<CategoryWithAmount> = emptyList()
)

data class ThisYearState(
    val thisYearIncome: Float = 0f,
    val thisYearExpense: Float = 0f,
    val thisYearTopIncomeCategory: String = "Other",
    val thisYearTopExpenseCategory: String = "Other",
    val thisYearGeneratedSummary: String = "",
    val isThisYearSummaryGenerated: Boolean = false,
    val isThisYearSummaryLoading: Boolean = false,
    val thisYearExpenseCategories: List<CategoryWithAmount> = emptyList(),
    val thisYearIncomeCategories: List<CategoryWithAmount> = emptyList()
)

fun StatisticsState.createTodayState(): TodayState {
    return TodayState(
        todayIncome = todayIncome,
        todayExpense = todayExpense,
        todayTopIncomeCategory = todayTopIncomeCategory,
        todayTopExpenseCategory = todayTopExpenseCategory,
        isTodaySummaryGenerated = isTodaySummaryGenerated,
        todayGeneratedSummary = todayGeneratedSummary,
        isTodaySummaryLoading = isTodaySummaryLoading,
        todayExpenseCategories = todayExpenseCategories,
        todayIncomeCategories = todayIncomeCategories
    )
}

fun StatisticsState.createThisMonthState(): ThisMonthState {
    return ThisMonthState(
        thisMonthIncome = thisMonthIncome,
        thisMonthExpense = thisMonthExpense,
        thisMonthTopIncomeCategory = thisMonthTopIncomeCategory,
        thisMonthTopExpenseCategory = thisMonthTopExpenseCategory,
        isThisMonthSummaryGenerated = isThisMonthSummaryGenerated,
        thisMonthGeneratedSummary = thisMonthGeneratedSummary,
        isThisMonthSummaryLoading = isThisMonthSummaryLoading,
        thisMonthExpenseCategories = thisMonthExpenseCategories,
        thisMonthIncomeCategories = thisMonthIncomeCategories
    )
}

fun StatisticsState.createThisYearState(): ThisYearState {
    return ThisYearState(
        thisYearIncome = thisYearIncome,
        thisYearExpense = thisYearExpense,
        thisYearTopIncomeCategory = thisYearTopIncomeCategory,
        thisYearTopExpenseCategory = thisMonthTopExpenseCategory,
        thisYearGeneratedSummary = thisYearGeneratedSummary,
        isThisYearSummaryGenerated = isThisYearSummaryGenerated,
        isThisYearSummaryLoading = isThisYearSummaryLoading,
        thisYearExpenseCategories = thisYearExpenseCategories,
        thisYearIncomeCategories = thisYearIncomeCategories
    )
}
