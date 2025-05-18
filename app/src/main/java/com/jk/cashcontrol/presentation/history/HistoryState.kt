package com.jk.cashcontrol.presentation.history

import com.jk.cashcontrol.domain.model.Transaction

data class HistoryState(
    val allTransactions : List<Transaction> = emptyList()
)
