package com.jk.cashcontrol.presentation.add_transaction

sealed interface AddTransactionAction {
    data class OnTextFieldValueChange(val amountTextFieldValue : String) : AddTransactionAction
    data object OnDateEditButtonClick : AddTransactionAction
    data object OnDatePickerDismiss : AddTransactionAction
    data class OnPickDateConfirmButton(val timestamp : String) : AddTransactionAction
    data class OnCategorySelection(val category : String = "Other") : AddTransactionAction
}