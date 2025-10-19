package com.jk.cashcontrol.features.expense_tracker.presentation.history

sealed interface HistoryAction {
    data object ReloadData : HistoryAction
}