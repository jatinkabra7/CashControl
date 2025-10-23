package com.jk.cashcontrol.features.expense_tracker.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.jk.cashcontrol.features.expense_tracker.data.repository.PAGE_SIZE
import com.jk.cashcontrol.features.expense_tracker.domain.model.Transaction
import com.jk.cashcontrol.features.expense_tracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    private var lastVisibleDocument: DocumentSnapshot? = null

    private var isLastPageReached = false

    init {
        getAllPaginatedTransactions()
    }

    fun addTransaction(transaction: Transaction) {
        _state.update {
            it.copy(
                allTransactions = listOf(transaction) + it.allTransactions
            )
        }
    }

    fun editTransactionName(transaction: Transaction, newName: String) {
        _state.update { historyState ->
            historyState.copy(
                allTransactions = historyState.allTransactions.map { it ->
                    if(it == transaction) {
                        it.copy(name = newName)
                    } else it
                }
            )
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        _state.update {
            it.copy(
                allTransactions = it.allTransactions.minus(transaction)
            )
        }
    }

    fun getAllPaginatedTransactions() {
        if(state.value.isLoading || isLastPageReached) {
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getTransactionPage(lastVisibleDocument)
                .onSuccess { page ->
                    _state.update { it.copy(
                        allTransactions = it.allTransactions + page.transactions,
                        isLoading = false
                    ) }

                    lastVisibleDocument = page.lastVisibleDocument

                    if(page.transactions.isEmpty() || page.transactions.size < PAGE_SIZE) {
                        isLastPageReached = true
                    }
                }
                .onFailure {

                }
        }
    }

    fun onAction(action : HistoryAction) {
        when(action) {
            is HistoryAction.ReloadData -> getAllPaginatedTransactions()
        }
    }
}