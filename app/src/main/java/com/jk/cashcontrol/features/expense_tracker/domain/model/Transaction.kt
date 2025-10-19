package com.jk.cashcontrol.features.expense_tracker.domain.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
data class Transaction (
    val name : String,
    val type : TransactionType,
    val amount : Float,
    val category: String,
    val timestamp : String,
    val timestampMillis : Long
)

@Keep
enum class TransactionType {INCOME, EXPENSE}