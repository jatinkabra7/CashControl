package com.jk.cashcontrol.app.presentation.navigation

import androidx.annotation.Keep
import com.jk.cashcontrol.features.expense_tracker.domain.model.TransactionType
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
    data object History : Route

    @Keep
    @Serializable
    data object Profile : Route

    @Keep
    @Serializable
    data object Login : Route

    @Keep
    @Serializable
    data object AppInfo: Route

    @Keep
    @Serializable
    data object AppLock: Route

    @Keep
    @Serializable
    data object StartResolver: Route
}