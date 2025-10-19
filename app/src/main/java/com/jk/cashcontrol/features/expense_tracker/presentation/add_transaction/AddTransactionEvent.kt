package com.jk.cashcontrol.features.expense_tracker.presentation.add_transaction

sealed interface AddTransactionEvent {
    data class ShowToast(val message: String) : AddTransactionEvent
}