package com.jk.cashcontrol.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.jk.cashcontrol.data.dto.TransactionDto
import com.jk.cashcontrol.data.mapper.toTransaction
import com.jk.cashcontrol.data.mapper.toTransactionDto
import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.domain.repository.TransactionRepository
import com.jk.cashcontrol.presentation.add_transaction.formatMillisToDate
import com.jk.cashcontrol.presentation.statistics.StatisticsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class TransactionRepositoryImpl(
    private val auth : FirebaseAuth,
    private val firestore : FirebaseFirestore
) : TransactionRepository {

    private val usersCollection get() = firestore.collection("users")
    private val uid get() = auth.currentUser?.uid.toString()
    private val uidDocument get() = usersCollection.document(uid)
    private val transactionsCollection get() = uidDocument.collection("transactions")


    override suspend fun insertUser(): Result<Boolean> {
        return try {
            val snapshot = uidDocument.get().await()
            if (!snapshot.exists()) {
                val userMap = mapOf(
                    "budget" to "0",
                    "expense" to "0"
                )
                uidDocument.set(userMap).await()
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllTransactions(): Flow<List<Transaction>> {
        return flow {
            try {

                transactionsCollection
                    .snapshots()
                    .collect { snapshot ->
                        val transactions =
                            snapshot.toObjects(TransactionDto::class.java).map { it.toTransaction() }
                        emit(transactions)
                    }

            } catch (e : Exception) {
                throw e
            }
        }
    }

    override suspend fun insertTransaction(transaction: Transaction): Result<Boolean> {

        return try {

            val documentRef = transactionsCollection.document()

            documentRef.set(
                transaction.toTransactionDto()
            ).await()

            Result.success(true)

        } catch (e : Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBudget(): Flow<Float> {
        return flow {
            try {

                uidDocument.snapshots().collect { snapshot ->
                    val budget = snapshot.getString("budget")?.toFloat() ?: 0f
                    emit(budget)
                }

            } catch (e : Exception) {
                throw e
            }
        }
    }

    override suspend fun getExpense(): Flow<Float> {
        return flow {
            try {

                uidDocument.snapshots().collect { snapshot ->
                    val expense = snapshot.getString("expense")?.toFloat() ?: 0f
                    emit(expense)
                }

            } catch (e : Exception) {
                throw e
            }
        }
    }

    override suspend fun updateBudget(budget: Float): Result<Boolean> {
        return try {

            uidDocument.update("budget", budget.toString()).await()

            Result.success(true)

        } catch (e : Exception) {
            Result.failure(e)
        }
    }

    override suspend fun makeNewBudget(budget: Float): Result<Boolean> {

        return try {

            uidDocument.update("budget", budget.toString()).await()
            uidDocument.update("expense","0").await()

            Result.success(true)

        } catch (e : Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateExpense(expense: Float): Result<Boolean> {
        return try {

            uidDocument.update("expense", expense.toString()).await()

            Result.success(true)

        } catch (e : Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getIncomeExpense(): Flow<StatisticsState> {
        return flow {
            try {

                transactionsCollection
                    .snapshots()
                    .collect { snapshot ->

                        var todayIncome = 0f
                        var todayExpense = 0f

                        var thisMonthIncome = 0f
                        var thisMonthExpense = 0f

                        var thisYearIncome = 0f
                        var thisYearExpense = 0f

                        val todayIncomeCategories = mutableMapOf<String, Float>()
                        val todayExpenseCategories = mutableMapOf<String, Float>()

                        val thisMonthIncomeCategories = mutableMapOf<String, Float>()
                        val thisMonthExpenseCategories = mutableMapOf<String, Float>()

                        val thisYearIncomeCategories = mutableMapOf<String, Float>()
                        val thisYearExpenseCategories = mutableMapOf<String, Float>()

                        var todayTopIncomeCategory = ""
                        var todayTopExpenseCategory = ""
                        var thisMonthTopIncomeCategory = ""
                        var thisMonthTopExpenseCategory = ""
                        var thisYearTopIncomeCategory = ""
                        var thisYearTopExpenseCategory = ""

                        val todayDate = System.currentTimeMillis().toString().formatMillisToDate()
                        val thisMonth = todayDate.substringAfter(" ")
                        val thisYear = todayDate.substringAfter(", ")

                        snapshot
                            .toObjects(TransactionDto::class.java)
                            .forEach {

                                val date = it.timestamp
                                val type = it.type
                                val amount = it.amount.toFloat()
                                val category = it.category

                                if(date == todayDate) {

                                    if(type == "INCOME") {
                                        todayIncome += amount
                                        todayIncomeCategories[category] = todayIncomeCategories.getOrDefault(category,0f) + amount
                                    }
                                    else {
                                        todayExpense += amount

                                        todayExpenseCategories[category] = todayExpenseCategories.getOrDefault(category,0f) + amount
                                    }
                                }

                                if(date.contains(thisMonth)) {
                                    if(type == "INCOME") {
                                        thisMonthIncome += amount
                                        thisMonthIncomeCategories[category] = thisMonthIncomeCategories.getOrDefault(category,0f) + amount

                                    }
                                    else {
                                        thisMonthExpense += amount
                                        thisMonthExpenseCategories[category] = thisMonthExpenseCategories.getOrDefault(category,0f) + amount
                                    }
                                }

                                if(date.contains(thisYear)) {
                                    if(type == "INCOME") {
                                        thisYearIncome += amount
                                        thisYearIncomeCategories[category] = thisYearIncomeCategories.getOrDefault(category,0f) + amount
                                    }
                                    else {
                                        thisYearExpense += amount
                                        thisYearExpenseCategories[category] = thisYearExpenseCategories.getOrDefault(category,0f) + amount
                                    }
                                }

                                todayTopIncomeCategory = todayIncomeCategories.maxByOrNull { it.value }?.key ?: ""
                                todayTopExpenseCategory = todayExpenseCategories.maxByOrNull { it.value }?.key ?: ""

                                thisMonthTopIncomeCategory = thisMonthIncomeCategories.maxByOrNull { it.value }?.key ?: ""
                                thisMonthTopExpenseCategory = thisMonthExpenseCategories.maxByOrNull { it.value }?.key ?: ""

                                thisYearTopIncomeCategory = thisYearIncomeCategories.maxByOrNull { it.value }?.key ?: ""
                                thisYearTopExpenseCategory = thisYearExpenseCategories.maxByOrNull { it.value }?.key ?: ""

                            }
                        emit(StatisticsState(
                            todayIncome = todayIncome,
                            todayExpense = todayExpense,
                            thisMonthIncome = thisMonthIncome,
                            thisMonthExpense = thisMonthExpense,
                            thisYearIncome = thisYearIncome,
                            thisYearExpense = thisYearExpense,
                            todayTopIncomeCategory = todayTopIncomeCategory,
                            todayTopExpenseCategory = todayTopExpenseCategory,
                            thisMonthTopIncomeCategory = thisMonthTopIncomeCategory,
                            thisMonthTopExpenseCategory = thisMonthTopExpenseCategory,
                            thisYearTopIncomeCategory = thisYearTopIncomeCategory,
                            thisYearTopExpenseCategory = thisYearTopExpenseCategory,
                        ))
                    }



            } catch (e : Exception) {
                throw e
            }
        }
    }
}