package com.jk.cashcontrol.features.expense_tracker.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.cashcontrol.features.expense_tracker.domain.repository.TransactionRepository
import com.jk.cashcontrol.features.expense_tracker.presentation.add_transaction.toMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    init {
        getAllTransactions()
    }

    fun getAllTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions()
                .distinctUntilChanged()
                .collect { transactions ->
                val allTransactions = transactions
                    .sortedWith(
                        Comparator { t1, t2 ->
                            val t1DateMillis = t1.timestamp.toMillis()
                            val t2DateMillis = t2.timestamp.toMillis()

                            val dateCompare = t2DateMillis.compareTo(t1DateMillis)

                            if (dateCompare != 0) {
                                dateCompare
                            } else {
                                t2.timestampMillis.toLong().compareTo(t1.timestampMillis.toLong())
                            }
                        }
                    )

                _state.update {
                    it.copy(allTransactions = allTransactions)
                }
            }
        }
    }

    fun onAction(action : HistoryAction) {
        when(action) {
            is HistoryAction.ReloadData -> getAllTransactions()
        }
    }
}