package com.jk.cashcontrol.features.expense_tracker.presentation.add_transaction

import com.jk.cashcontrol.features.expense_tracker.domain.model.TransactionType
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class AddTransactionState(
    val transactionType: TransactionType? = null,
    val category : String = "Other",
    val amountTextFieldValue : String = "",
    val isDatePickerDialogOpen : Boolean = false,
    val timestamp : String = System.currentTimeMillis().toString().formatMillisToDate(),
    val nameTextFieldValue : String = "New Transaction"
)

fun String.formatMillisToDate() : String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.getDefault())
    return Instant.ofEpochMilli(this.toLong())
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}

fun String.toMillis() : Long {
    val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.getDefault())
    return LocalDate
        .parse(this, formatter)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}
