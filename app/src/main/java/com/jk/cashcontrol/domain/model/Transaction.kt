package com.jk.cashcontrol.domain.model

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

enum class TransactionType {INCOME, EXPENSE}