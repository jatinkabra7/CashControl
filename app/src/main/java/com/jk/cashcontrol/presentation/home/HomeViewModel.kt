package com.jk.cashcontrol.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.cashcontrol.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        getBudget()
        getExpense()
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
                        it.copy(expense = expense)
                    }
                }
        }
    }


}