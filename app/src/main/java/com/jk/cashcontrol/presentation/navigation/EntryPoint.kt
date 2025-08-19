package com.jk.cashcontrol.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.jk.cashcontrol.presentation.theme.BackgroundColor
import com.jk.cashcontrol.presentation.theme.CashControlTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPoint() {
    CashControlTheme {

        val navController = rememberNavController()
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route

        var showBottomBar = when {
            currentRoute == null -> false
            currentRoute.contains("AddTransaction") -> false
            currentRoute.contains("TransactionInfo") -> false
            currentRoute.contains("AppLock") -> false
            currentRoute.contains("AppInfo") -> false
            currentRoute.contains("StartResolver") -> false
            currentRoute.contains("Login", ignoreCase = true) -> false
            else -> true
        }

        if(Firebase.auth.currentUser == null) showBottomBar = false

        Scaffold(
            containerColor = BackgroundColor,
            bottomBar = {
                BottomBar(
                    show = showBottomBar,
                    currentRoute = currentRoute,
                    onClick = { navController.navigate(it) { popUpTo(0) } }
                )
            }
        ) { innerPadding ->
            NavGraph(
                navController = navController,
                paddingValues = innerPadding
            )
        }
    }
}