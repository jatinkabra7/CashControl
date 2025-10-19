package com.jk.cashcontrol.features.expense_tracker.data.mapper

import com.jk.cashcontrol.features.expense_tracker.data.dto.TransactionDto
import com.jk.cashcontrol.features.expense_tracker.domain.model.Transaction
import com.jk.cashcontrol.features.expense_tracker.domain.model.TransactionType

fun TransactionDto.toTransaction() = Transaction(
    name = name,
    amount = amount.toFloat(),
    category = category,
    timestamp = timestamp,
    type = if(type == "INCOME") TransactionType.INCOME else TransactionType.EXPENSE,
    timestampMillis = timestampMillis
)

fun Transaction.toTransactionDto() = TransactionDto(
    name = name,
    amount = amount.toString(),
    category = category,
    timestamp = timestamp,
    type = type.toString(),
    timestampMillis = timestampMillis
)