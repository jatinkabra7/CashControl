package com.jk.cashcontrol.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.cashcontrol.domain.model.TransactionType
import com.jk.cashcontrol.domain.repository.TransactionRepository
import com.jk.cashcontrol.presentation.add_transaction.toMillis
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class HomeViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _event = Channel<HomeEvents>()
    val event = _event.receiveAsFlow()

    init {
        initialiseData()
    }

    fun initialiseData() {
        getBudget()
        getExpense()
        getRecentTransactions()
    }

    fun onAction(action: HomeActions) {
        when (action) {
            HomeActions.ReloadData -> {
                initialiseData()
            }

            is HomeActions.OnEditBudgetClick -> {
                _state.update {
                    it.copy(
                        editBudgetTextFieldValue = state.value.budget.toString(),
                        isEditBudgetDialogOpen = true
                    )
                }
            }

            is HomeActions.OnEditBudgetConfirm -> {
                viewModelScope.launch {

                    _state.update { it.copy(isEditBudgetDialogOpen = false) }
                    repository.updateBudget(action.newBudget)
                }
            }

            HomeActions.OnEditBudgetDismiss -> {
                _state.update { it.copy(isEditBudgetDialogOpen = false) }
            }

            is HomeActions.OnEditBudgetTextFieldValueChange -> {
                val value = action.value

                if (isValidAmount(value)) {
                    _state.update { it.copy(editBudgetTextFieldValue = value) }
                }
            }

            HomeActions.OnNewBudgetClick -> {
                _state.update {
                    it.copy(
                        newBudgetTextFieldValue = state.value.budget.toString(),
                        isNewBudgetDialogOpen = true
                    )
                }
            }

            is HomeActions.OnNewBudgetConfirm -> {

                _state.update { it.copy(isNewBudgetDialogOpen = false) }

                viewModelScope.launch {
                    repository.updateExpense(0f)
                    repository.updateBudget(action.newBudget)
                }
            }

            HomeActions.OnNewBudgetDismiss -> {
                _state.update { it.copy(isNewBudgetDialogOpen = false) }
            }

            is HomeActions.OnNewBudgetTextFieldValueChange -> {
                val value = action.value

                if (isValidAmount(value)) {
                    _state.update { it.copy(newBudgetTextFieldValue = value) }
                }
            }
        }
    }

    fun onEvent(event: HomeEvents) {
        when(event) {
            is HomeEvents.ShowToast -> {
                _event.trySend(event)
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
            amount <= 999_999_99.99
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun getBudget() {
        viewModelScope.launch {
            repository.getBudget()
                .collect { budget ->
                    _state.update {
                        it.copy(budget = budget)
                    }
                }
        }
    }

    fun getExpense() {
        viewModelScope.launch {
            repository.getExpense()
                .collect { expense ->
                    _state.update {
                        val remaining =
                            if (state.value.budget - expense >= 0) state.value.budget - expense else 0f
                        it.copy(expense = expense, remaining = remaining)
                    }
                }
        }
    }

    fun getRecentTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions()
                .distinctUntilChanged()
                .collect { transactions ->
                    val recentTransactions = transactions
                        .sortedWith(
                            Comparator { t1, t2 ->
                                val t1DateMillis = t1.timestamp.toMillis()
                                val t2DateMillis = t2.timestamp.toMillis()

                                val dateCompare = t2DateMillis.compareTo(t1DateMillis)

                                if (dateCompare != 0) {
                                    dateCompare
                                } else {
                                    t2.timestampMillis.toLong()
                                        .compareTo(t1.timestampMillis.toLong())
                                }
                            }
                        )
                        .take(5)

                    _state.update {
                        it.copy(recentTransactions = recentTransactions)
                    }
                }
        }
    }

    fun updateExpense(expense: Float, type: TransactionType) {
        viewModelScope.launch {

            val oldExpense = state.value.expense

            val newExpense = expense

            val totalExpense = if (type == TransactionType.EXPENSE) {
                oldExpense + newExpense
            } else {
                oldExpense
            }

            repository.updateExpense(totalExpense)

            _state.update {
                it.copy(expense = totalExpense)
            }
        }
    }
}