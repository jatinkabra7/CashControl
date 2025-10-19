package com.jk.cashcontrol.features.expense_tracker.presentation.add_transaction

import com.jk.cashcontrol.features.expense_tracker.domain.model.Transaction

sealed interface AddTransactionAction {
    data class OnAmountTextFieldValueChange(val amountTextFieldValue : String) : AddTransactionAction
    data class OnNameTextFieldValueChange(val nameTextFieldValue : String) : AddTransactionAction
    data object OnDateEditButtonClick : AddTransactionAction
    data object OnDatePickerDismiss : AddTransactionAction
    data class OnPickDateConfirmButton(val timestamp : String) : AddTransactionAction
    data class OnCategorySelection(val category : String = "Other") : AddTransactionAction
    data class OnSubmit(val transaction : Transaction) : AddTransactionAction
}