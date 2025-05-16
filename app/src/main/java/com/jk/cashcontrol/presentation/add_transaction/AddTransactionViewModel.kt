package com.jk.cashcontrol.presentation.add_transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.cashcontrol.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddTransactionViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddTransactionState())
    val state = _state.asStateFlow()

    fun onAction(action : AddTransactionAction) {
        when(action) {
            is AddTransactionAction.OnTextFieldValueChange -> {

                val newValue = action.amountTextFieldValue

                if(isValidAmount(newValue)) {

                    _state.update {
                        it.copy(
                            amountTextFieldValue = action.amountTextFieldValue
                        )
                    }
                }
            }

            AddTransactionAction.OnDateEditButtonClick -> {
                _state.update {
                    it.copy(isDatePickerDialogOpen = true)
                }
            }
            AddTransactionAction.OnDatePickerDismiss -> {
                _state.update {
                    it.copy(isDatePickerDialogOpen = false)
                }
            }
            is AddTransactionAction.OnPickDateConfirmButton -> {

                val date = action.timestamp.formatMillisToDate()

                _state.update {
                    it.copy(isDatePickerDialogOpen = false, timestamp = date)
                }
            }

            is AddTransactionAction.OnCategorySelection -> {
                _state.update { it.copy(category = action.category) }
            }
        }
    }

    fun updateTransactionType(transactionType: TransactionType) {
        viewModelScope.launch {
            _state.update {
                it.copy(transactionType = transactionType)
            }
        }
    }

    private fun isValidAmount(input: String): Boolean {
        // Allow empty input (so user can clear it)
        if (input.isEmpty()) return true

        // Regex to match a number with up to 2 decimal places
        val regex = Regex("^\\d*\\.?\\d{0,2}$")
        if (!regex.matches(input)) return false

        // Try parsing to float and check if it’s within limit
        return try {
            val amount = input.toFloat()
            amount <= 999_999.99
        } catch (e: NumberFormatException) {
            false
        }
    }
}