package com.jk.cashcontrol.features.expense_tracker.domain.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.jk.cashcontrol.features.expense_tracker.data.model.TransactionPage
import com.jk.cashcontrol.features.expense_tracker.domain.model.Transaction
import com.jk.cashcontrol.features.expense_tracker.presentation.statistics.StatisticsState
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun insertUser() : Result<Boolean>
    suspend fun deleteUserData(): Result<Boolean>

    // Paginated transactions
    suspend fun getTransactionPage(lastVisibleDocument: DocumentSnapshot?) : Result<TransactionPage>
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