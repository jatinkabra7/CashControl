package com.jk.cashcontrol.app.presentation.navigation

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed interface NavigationGraphs {

    @Keep
    @Serializable
    data object AuthGraph: NavigationGraphs

    @Keep
    @Serializable
    data object ExpenseTrackerGraph: NavigationGraphs
}