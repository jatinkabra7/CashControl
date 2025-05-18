package com.jk.cashcontrol.data.repository

import androidx.compose.foundation.content.TransferableContent
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.jk.cashcontrol.data.dto.TransactionDto
import com.jk.cashcontrol.data.mapper.toTransaction
import com.jk.cashcontrol.data.mapper.toTransactionDto
import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlin.math.exp

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
}