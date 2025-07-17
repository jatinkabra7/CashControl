package com.jk.cashcontrol.data.dto

import androidx.annotation.Keep
import com.jk.cashcontrol.presentation.add_transaction.formatMillisToDate

@Keep
data class TransactionDto(
    val timestamp : String = System.currentTimeMillis().toString().formatMillisToDate(),
    val category: String = "",
    val name : String = "",
    val type : String = "",
    val amount : String = "",
    val timestampMillis : Long = System.currentTimeMillis()
)