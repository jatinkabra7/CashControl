package com.jk.cashcontrol.presentation.add_transaction

interface AddTransactionEvent {
    data class ShowToast(val message: String) : AddTransactionEvent
}