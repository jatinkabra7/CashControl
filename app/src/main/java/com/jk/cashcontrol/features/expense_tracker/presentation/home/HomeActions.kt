package com.jk.cashcontrol.features.expense_tracker.presentation.home

sealed interface HomeActions {
    data object ReloadData : HomeActions
    data object OnEditBudgetClick : HomeActions
    data class OnEditBudgetConfirm(val newBudget : Float) : HomeActions
    data object OnEditBudgetDismiss : HomeActions
    data class OnEditBudgetTextFieldValueChange(val value : String) : HomeActions
    data object OnNewBudgetClick : HomeActions
    data class OnNewBudgetConfirm(val newBudget: Float) : HomeActions
    data object OnNewBudgetDismiss : HomeActions
    data class OnNewBudgetTextFieldValueChange(val value : String) : HomeActions
}