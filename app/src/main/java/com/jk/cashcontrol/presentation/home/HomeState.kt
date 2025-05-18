package com.jk.cashcontrol.presentation.home

import com.jk.cashcontrol.domain.model.Transaction

data class HomeState(
    val expense : Float = 0f,
    val budget : Float = 0f,
    val recentTransactions : List<Transaction> = emptyList(),
    val username : String = "Username",
    val isBottomSheetOpen : Boolean = false,
    val isEditBudgetDialogOpen : Boolean = false,
    val editBudgetTextFieldValue : String = "0",
    val isNewBudgetDialogOpen : Boolean = false,
    val newBudgetTextFieldValue : String = "0",
    val allTransactions : List<Transaction> = emptyList()
)
