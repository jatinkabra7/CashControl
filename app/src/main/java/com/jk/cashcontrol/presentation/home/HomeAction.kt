package com.jk.cashcontrol.presentation.home

sealed interface HomeAction {
    data object ReloadData : HomeAction
    data object OnEditBudgetClick : HomeAction
    data class OnEditBudgetConfirm(val newBudget : Float) : HomeAction
    data object OnEditBudgetDismiss : HomeAction
    data class OnEditBudgetTextFieldValueChange(val value : String) : HomeAction
    data object OnNewBudgetClick : HomeAction
    data class OnNewBudgetConfirm(val newBudget: Float) : HomeAction
    data object OnNewBudgetDismiss : HomeAction
    data class OnNewBudgetTextFieldValueChange(val value : String) : HomeAction
}