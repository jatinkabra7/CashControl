package com.jk.cashcontrol.presentation.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.jk.cashcontrol.domain.model.TransactionType
import com.jk.cashcontrol.presentation.theme.CashControlTheme
import kotlin.text.contains

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPoint() {
    CashControlTheme {

        val navController = rememberNavController()
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route

        var showSheet by remember { mutableStateOf(false) }

        var showBottomBar = when {
            currentRoute == null -> true
            currentRoute.contains("AddTransaction") -> false
            currentRoute.contains("Login", ignoreCase = true) -> false
            else -> true
        }

        if(Firebase.auth.currentUser == null) showBottomBar = false

        val bottomSheetState = rememberModalBottomSheetState()

        Scaffold(
            containerColor = Color.Black,
            bottomBar = {

                BottomBar(
                    show = showBottomBar,
                    currentRoute = currentRoute,
                    onClick = {
                        when (it) {
                            is Route.AddTransactionEntry -> showSheet = true
                            else -> navController.navigate(it) {
                                popUpTo(0)
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavGraph(
                navController = navController,
                paddingValues = innerPadding
            )

            // BottomSheet
            if (showSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showSheet = false
                    },
                    sheetState = bottomSheetState,
                    containerColor = Color.Black
                ) {
                    BottomSheetContent(
                        onIncomeButtonClick = {
                            showSheet = false
                            navController.navigate(Route.AddTransaction(transactionType = TransactionType.INCOME))
                        },
                        onExpenseButtonClick = {
                            showSheet = false
                            navController.navigate(Route.AddTransaction(transactionType = TransactionType.EXPENSE))
                        }
                    )
                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    }
}