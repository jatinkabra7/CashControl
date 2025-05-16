package com.jk.cashcontrol.presentation.navigation

import com.jk.cashcontrol.domain.model.TransactionType
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object Home : Route

    @Serializable
    data object Statistics : Route

    @Serializable
    data class AddTransaction(val transactionType: TransactionType) : Route

    @Serializable
    data object AddTransactionEntry : Route

    @Serializable
    data object History : Route

    @Serializable
    data object Profile : Route

    @Serializable
    data object Login : Route
}