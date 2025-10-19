package com.jk.cashcontrol.app.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.firebase.auth.FirebaseAuth
import com.jk.cashcontrol.features.auth.presentation.app_lock.AppLockViewModel
import com.jk.cashcontrol.features.auth.presentation.login.LoginViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.history.HistoryViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.home.HomeViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.settings.SettingsViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.statistics.StatisticsViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.transaction.TransactionInfoViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RootGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    bannerAd: @Composable () -> Unit,
    loginViewModel: LoginViewModel = koinViewModel(),
    appLockViewModel: AppLockViewModel = koinViewModel(),
    homeViewModel: HomeViewModel = koinViewModel(),
    statisticsViewModel: StatisticsViewModel = koinViewModel(),
    historyViewModel: HistoryViewModel = koinViewModel(),
    transactionInfoViewModel: TransactionInfoViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val appLockStatus = appLockViewModel.appLockStatus.collectAsStateWithLifecycle().value

    val appLockPin = appLockViewModel.appLockPin.collectAsStateWithLifecycle().value

    val user = FirebaseAuth.getInstance().currentUser

    val start =
        if (user != null) {
            Route.StartResolver
        } else Route.Login

    NavHost(
        navController = navController,
        startDestination = NavigationGraphs.AuthGraph,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {
        authGraph(
            navController = navController,
            paddingValues = paddingValues,
            start = start,
            context = context,
            appLockStatus = appLockStatus,
            appLockPin = appLockPin,
            loginViewModel = loginViewModel,
            appLockViewModel = appLockViewModel
        )
        expenseTrackerGraph(
            navController = navController,
            paddingValues = paddingValues,
            user = user,
            appLockStatus = appLockStatus,
            context = context,
            bannerAd = bannerAd,
            homeViewModel = homeViewModel,
            statisticsViewModel = statisticsViewModel,
            historyViewModel = historyViewModel,
            transactionInfoViewModel = transactionInfoViewModel,
            settingsViewModel = settingsViewModel,
        )
    }
}