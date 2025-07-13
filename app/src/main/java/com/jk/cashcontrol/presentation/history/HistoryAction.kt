package com.jk.cashcontrol.presentation.history

sealed interface HistoryAction {
    data object ReloadData : HistoryAction
}