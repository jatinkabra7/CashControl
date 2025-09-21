package com.jk.cashcontrol.presentation.add_transaction

sealed interface AddTransactionEvent {
    data class ShowToast(val message: String) : AddTransactionEvent
}