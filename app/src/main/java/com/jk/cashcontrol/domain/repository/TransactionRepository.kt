package com.jk.cashcontrol.domain.repository

import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.presentation.statistics.StatisticsState
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun insertUser() : Result<Boolean>
    suspend fun deleteUserData(): Result<Boolean>

    suspend fun getAllTransactions() : Flow<List<Transaction>>
    suspend fun insertTransaction(transaction : Transaction) : Result<Boolean>
    suspend fun deleteTransaction(transaction : Transaction) : Result<Boolean>
    suspend fun editTransactionName(transaction : Transaction, newName: String) : Result<Boolean>
    suspend fun getBudget() : Flow<Float>
    suspend fun getExpense() : Flow<Float>
    suspend fun updateBudget(budget : Float) : Result<Boolean>
    suspend fun updateExpense(expense : Float) : Result<Boolean>
    suspend fun makeNewBudget(budget : Float) : Result<Boolean>

    suspend fun getIncomeExpense() : Flow<StatisticsState>
}