package com.jk.cashcontrol.app.presentation.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jk.cashcontrol.core.presentation.ObserveAsEvents
import com.jk.cashcontrol.features.auth.domain.model.User
import com.jk.cashcontrol.features.auth.presentation.login.LoginViewModel
import com.jk.cashcontrol.features.expense_tracker.domain.model.TransactionType
import com.jk.cashcontrol.features.expense_tracker.presentation.add_transaction.AddTransactionEvent
import com.jk.cashcontrol.features.expense_tracker.presentation.add_transaction.AddTransactionScreen
import com.jk.cashcontrol.features.expense_tracker.presentation.add_transaction.AddTransactionViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.app_info.AppInfoScreen
import com.jk.cashcontrol.features.expense_tracker.presentation.history.HistoryScreen
import com.jk.cashcontrol.features.expense_tracker.presentation.history.HistoryViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.home.HomeEvents
import com.jk.cashcontrol.features.expense_tracker.presentation.home.HomeScreen
import com.jk.cashcontrol.features.expense_tracker.presentation.home.HomeViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.profile.ProfileScreen
import com.jk.cashcontrol.features.expense_tracker.presentation.settings.SettingsViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.statistics.StatisticsScreen
import com.jk.cashcontrol.features.expense_tracker.presentation.statistics.StatisticsViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.transaction.TransactionInfoViewModel
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.expenseTrackerGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    user: FirebaseUser?,
    appLockStatus: Boolean?,
    context: Context,
    homeViewModel: HomeViewModel,
    statisticsViewModel: StatisticsViewModel,
    historyViewModel: HistoryViewModel,
    transactionInfoViewModel: TransactionInfoViewModel,
    settingsViewModel: SettingsViewModel
) {
    navigation<NavigationGraphs.ExpenseTrackerGraph>(startDestination = Route.Home) {
        composable<Route.Home> {

            val state = homeViewModel.state.collectAsStateWithLifecycle().value

            val isDeletionInProgress =
                transactionInfoViewModel.isLoading.collectAsStateWithLifecycle().value

            var isTransactionInfoSheetOpen by remember { mutableStateOf(false) }

            var lastToastTime = 0L

            ObserveAsEvents(homeViewModel.event) {
                when(it) {
                    is HomeEvents.ShowToast -> {
                        val now = System.currentTimeMillis()
                        if(now - lastToastTime > 4000) {
                            lastToastTime = now
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            HomeScreen(
                state = state,
                user = user,
                modifier = Modifier.padding(paddingValues),
                onAction = { homeViewModel.onAction(it) },
                onEvent = { homeViewModel.onEvent(it) },
                onIncomeButtonClick = { navController.navigate(Route.AddTransaction(TransactionType.INCOME)) },
                onExpenseButtonClick = { navController.navigate(Route.AddTransaction(TransactionType.EXPENSE)) },
                isTransactionInfoSheetOpen = isTransactionInfoSheetOpen,
                toggleSheet = { isTransactionInfoSheetOpen = !isTransactionInfoSheetOpen },
                isDeletionInProgress = isDeletionInProgress,
                onDeleteTransaction = { transaction ->
                    transactionInfoViewModel.deleteTransaction(
                        context = context,
                        transaction = transaction,
                        onSuccess = { isTransactionInfoSheetOpen = false }
                    )
                },
                onEditTransactionName = { transaction, newName ->
                    transactionInfoViewModel.editTransactionName(
                        context = context,
                        transaction = transaction,
                        newName = newName,
                        onSuccess = { isTransactionInfoSheetOpen = false }
                    )
                }
            )
        }

        composable<Route.AddTransaction> { navBackStackEntry ->

            val transactionType = navBackStackEntry.toRoute<Route.AddTransaction>().transactionType

            val addTransactionViewModel = koinViewModel<AddTransactionViewModel>()

            addTransactionViewModel.updateTransactionType(transactionType)

            val state = addTransactionViewModel.state.collectAsStateWithLifecycle().value

            var lastToastTime = 0L

            ObserveAsEvents(addTransactionViewModel.event) {
                when (it) {
                    is AddTransactionEvent.ShowToast -> {
                        val now = System.currentTimeMillis()
                        if(now - lastToastTime > 4000) {
                            lastToastTime = now
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            AddTransactionScreen(
                state = state,
                onAction = { addTransactionViewModel.onAction(it) },
                onEvent = { addTransactionViewModel.onEvent(it) },
                navigateToHome = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.AddTransaction(transactionType)) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.Statistics> {

            val state = statisticsViewModel.state.collectAsStateWithLifecycle().value

            StatisticsScreen(
                modifier = Modifier.padding(paddingValues),
                state = state,
                user = user,
                onAction = { statisticsViewModel.onAction(it) }
            )
        }

        composable<Route.History> {

            val state = historyViewModel.state.collectAsStateWithLifecycle().value

            val isDeletionInProgress =
                transactionInfoViewModel.isLoading.collectAsStateWithLifecycle().value

            var isTransactionInfoSheetOpen by remember { mutableStateOf(false) }

            HistoryScreen(
                modifier = Modifier.padding(paddingValues),
                state = state,
                user = user,
                onAction = { historyViewModel.onAction(it) },
                isTransactionInfoSheetOpen = isTransactionInfoSheetOpen,
                toggleSheet = { isTransactionInfoSheetOpen = !isTransactionInfoSheetOpen },
                isDeletionInProgress = isDeletionInProgress,
                onDeleteTransaction = { transaction ->
                    transactionInfoViewModel.deleteTransaction(
                        context = context,
                        transaction = transaction,
                        onSuccess = { isTransactionInfoSheetOpen = false }
                    )
                },
                onEditTransactionName = { transaction, newName ->
                    transactionInfoViewModel.editTransactionName(
                        context = context,
                        transaction = transaction,
                        newName = newName,
                        onSuccess = { isTransactionInfoSheetOpen = false }
                    )
                }
            )
        }

        composable<Route.Profile> {

            val loginViewModel: LoginViewModel = koinViewModel()

            val appUser = if (user != null) {
                User(
                    imageUrl = user.photoUrl.toString(),
                    id = user.uid,
                    name = user.displayName.toString(),
                    email = user.email.toString()
                )
            } else User()

            val isAccountDeleting = loginViewModel.isDeleting.collectAsStateWithLifecycle().value

            ProfileScreen(
                user = appUser,
                appLockStatus = appLockStatus!!,
                onAction = settingsViewModel::onAction,
                navigateToAppInfoScreen = { navController.navigate(Route.AppInfo) },
                navigateToAppLockScreen = { navController.navigate(Route.AppLock) },
                onLogout = {
                    settingsViewModel.disableAppLock()
                    settingsViewModel.savePin("")
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Route.Login) {
                        popUpTo(Route.Profile) { inclusive = true }
                    }
                },
                onDeleteAccount = {
                    loginViewModel.deleteAccount(
                        context,
                        FirebaseAuth.getInstance(),
                        navigateToLogin = {
                            navController.navigate(Route.Login) {
                                popUpTo(Route.Profile) { inclusive = true }
                            }
                        }
                    )
                },
                isAccountDeleting = isAccountDeleting,
                modifier = Modifier.padding(paddingValues)
            )
        }

        composable<Route.AppInfo> {
            AppInfoScreen(
                navigateUp = { navController.navigateUp() },
                modifier = Modifier.padding(paddingValues)
            )
        }
    }

}