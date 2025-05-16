package com.jk.cashcontrol.domain.model

data class Transaction(
    val timestamp : String,
    val category: String,
    val name : String,
    val type : TransactionType,
    val amount : Float
)
enum class TransactionType {INCOME, EXPENSE}