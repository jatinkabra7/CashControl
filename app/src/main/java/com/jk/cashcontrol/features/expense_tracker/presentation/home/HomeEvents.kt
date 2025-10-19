package com.jk.cashcontrol.features.expense_tracker.presentation.home

sealed interface HomeEvents {
    data class ShowToast(val message: String) : HomeEvents
}