package com.jk.cashcontrol.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.jk.cashcontrol.domain.repository.TransactionRepository
import com.jk.cashcontrol.presentation.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StatisticsState())
    val state = _state.asStateFlow()

    init {
        getTodayIncomeExpense()
    }

    val aiModel = GenerativeModel(
        modelName = "gemini-1.5-flash-001",
        apiKey = Constants.GEMINI_API_KEY
    )

    val initialisingMessage =
        "You are Cash Control AI. Cash Control is an expense tracking app." +
                "The currency is unknown. Do not use any currency symbol like $ or Rupees " +
                " We will send you the data of expense and income. Generate a summary or report which describes the current situation and what can be done further " +
                "Provide short and crisp reports. "


    suspend fun getTodayReport(state : StatisticsState) {

        val todayReport =
            "today income = ${state.todayIncome} " +
                    "today expense = ${state.todayExpense} " +
                    "today top income category = ${state.todayTopIncomeCategory} " +
                    "today top expense category = ${state.todayTopExpenseCategory}"

        val response = aiModel.startChat().sendMessage(initialisingMessage + todayReport).text?: "Something Went Wrong."

        _state.update {
            it.copy(
                isTodaySummaryGenerated = true,
                todayGeneratedSummary = response
            )
        }
    }

    suspend fun getThisMonthReport(state : StatisticsState) {

        val thisMonthReport =
            "this month income = ${state.thisMonthIncome} " +
                    "this month expense = ${state.thisMonthExpense} " +
                    "this month top income category = ${state.thisMonthTopIncomeCategory} " +
                    "this month top expense category = ${state.thisMonthTopExpenseCategory}"

        val response = aiModel.startChat().sendMessage(initialisingMessage + thisMonthReport).text?: "Something Went Wrong."

        _state.update {
            it.copy(
                isThisMonthSummaryGenerated = true,
                thisMonthGeneratedSummary = response
            )
        }
    }

    suspend fun getThisYearReport(state : StatisticsState) {

        val thisYearReport =
            "this year income = ${state.thisYearIncome} " +
                    "this year expense = ${state.thisYearExpense} " +
                    "this year top income category = ${state.thisYearTopIncomeCategory} " +
                    "this year top expense category = ${state.thisYearTopExpenseCategory}"

        val response = aiModel.startChat().sendMessage(initialisingMessage + thisYearReport).text?: "Something Went Wrong."

        _state.update {
            it.copy(
                isThisYearSummaryGenerated = true,
                thisYearGeneratedSummary = response
            )
        }
    }

    fun getTodayIncomeExpense() {
        viewModelScope.launch {
            repository.getIncomeExpense().collect { newState ->

                _state.update {
                    it.copy(
                        todayIncome = newState.todayIncome,
                        todayExpense = newState.todayExpense,
                        thisMonthIncome = newState.thisMonthIncome,
                        thisMonthExpense = newState.thisMonthExpense,
                        thisYearIncome = newState.thisYearIncome,
                        thisYearExpense = newState.thisYearExpense,
                        todayTopIncomeCategory = newState.todayTopIncomeCategory,
                        todayTopExpenseCategory = newState.todayTopExpenseCategory,
                        thisMonthTopIncomeCategory = newState.thisMonthTopIncomeCategory,
                        thisMonthTopExpenseCategory = newState.thisMonthTopExpenseCategory,
                        thisYearTopIncomeCategory = newState.thisYearTopIncomeCategory,
                        thisYearTopExpenseCategory = newState.thisYearTopExpenseCategory
                    )
                }
            }
        }
    }

    fun onAction(action: StatisticsAction) {
        viewModelScope.launch {

            when (action) {
                is StatisticsAction.ReloadData -> getTodayIncomeExpense()
                is StatisticsAction.OnTodayGenerateSummaryClick -> {
                    _state.update {
                        it.copy(isTodaySummaryLoading = true)
                    }
                    getTodayReport(action.state)

                    _state.update {
                        it.copy(isTodaySummaryLoading = false)
                    }

                }
                is StatisticsAction.OnThisMonthGenerateSummaryClick -> {
                    _state.update {
                        it.copy(isThisMonthSummaryLoading = true)
                    }
                    getThisMonthReport(action.state)

                    _state.update {
                        it.copy(isThisMonthSummaryLoading = false)
                    }

                }
                is StatisticsAction.OnThisYearGenerateSummaryClick -> {
                    _state.update { it.copy(isThisYearSummaryLoading = true) }
                    getThisYearReport(action.state)
                    _state.update { it.copy(isThisYearSummaryLoading = false) }
                }
            }
        }
    }
}