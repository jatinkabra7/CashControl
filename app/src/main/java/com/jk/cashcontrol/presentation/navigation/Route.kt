package com.jk.cashcontrol.presentation.navigation

import androidx.annotation.Keep
import com.jk.cashcontrol.domain.model.TransactionType
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed interface Route {

    @Keep
    @Serializable
    data object Home : Route

    @Keep
    @Serializable
    data object Statistics : Route

    @Keep
    @Serializable
    data class AddTransaction(val transactionType: TransactionType) : Route

    @Keep
    @Serializable
    data object AddTransactionEntry : Route

    @Keep
    @Serializable
    data object History : Route

    @Keep
    @Serializable
    data object Profile : Route

    @Keep
    @Serializable
    data object Login : Route

    @Keep
    @Serializable
    data class TransactionInfo(
        val name : String,
        val type : TransactionType,
        val amount : Float,
        val category: String,
        val timestamp : String,
        val timestampMillis: Long
    ): Route
}