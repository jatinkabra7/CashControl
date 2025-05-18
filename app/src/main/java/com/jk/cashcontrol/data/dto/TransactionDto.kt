package com.jk.cashcontrol.data.dto

import com.jk.cashcontrol.presentation.add_transaction.formatMillisToDate

data class TransactionDto(
    val timestamp : String = System.currentTimeMillis().toString().formatMillisToDate(),
    val category: String = "",
    val name : String = "",
    val type : String = "",
    val amount : String = "",
    val timestampMillis : Long = System.currentTimeMillis()
)