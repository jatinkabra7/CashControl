package com.jk.cashcontrol.presentation.transaction

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionInfoViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun deleteTransaction(transaction: Transaction, context: Context, navigateUp: () -> Unit) {

        viewModelScope.launch {

            _isLoading.value = true

            transactionRepository.deleteTransaction(transaction)
                .onSuccess {
                    navigateUp()
                }
                .onFailure {
                    Toast.makeText(context,it.message, Toast.LENGTH_LONG).show()
                }

            _isLoading.value = false
        }
    }
}