package com.jk.cashcontrol.features.expense_tracker.presentation.history

import com.jk.cashcontrol.features.expense_tracker.domain.model.Transaction

data class HistoryState(
    val allTransactions : List<Transaction> = emptyList(),
    val isLoading: Boolean = false
)
