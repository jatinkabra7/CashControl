package com.jk.cashcontrol.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import com.jk.cashcontrol.domain.model.TransactionType
import com.jk.cashcontrol.domain.model.User
import com.jk.cashcontrol.presentation.add_transaction.AddTransactionScreen
import com.jk.cashcontrol.presentation.add_transaction.AddTransactionViewModel
import com.jk.cashcontrol.presentation.app_info.AppInfoScreen
import com.jk.cashcontrol.presentation.app_lock.AppLockScreen
import com.jk.cashcontrol.presentation.app_lock.AppLockViewModel
import com.jk.cashcontrol.presentation.history.HistoryScreen
import com.jk.cashcontrol.presentation.history.HistoryViewModel
import com.jk.cashcontrol.presentation.home.HomeScreen
import com.jk.cashcontrol.presentation.home.HomeViewModel
import com.jk.cashcontrol.presentation.login.LoginScreen
import com.jk.cashcontrol.presentation.login.LoginViewModel
import com.jk.cashcontrol.presentation.profile.ProfileScreen
import com.jk.cashcontrol.presentation.settings.SettingsViewModel
import com.jk.cashcontrol.presentation.statistics.StatisticsScreen
import com.jk.cashcontrol.presentation.statistics.StatisticsViewModel
import com.jk.cashcontrol.presentation.transaction.TransactionInfoViewModel
import com.jk.cashcontrol.presentation.utils.fingerprint_login.BiometricPromptManager
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel = koinViewModel(),
    loginViewModel: LoginViewModel = koinViewModel(),
    historyViewModel: HistoryViewModel = koinViewModel(),
    statisticsViewModel: StatisticsViewModel = koinViewModel(),
    transactionInfoViewModel: TransactionInfoViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel(),
    appLockViewModel: AppLockViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val activity = context as FragmentActivity

    val user = FirebaseAuth.getInstance().currentUser

    val appLockStatus = settingsViewModel.appLockStatus.collectAsStateWithLifecycle().value

    val appLockPin = appLockViewModel.appLockPin.collectAsStateWithLifecycle().value

    val biometricPromptManager = BiometricPromptManager(activity)

    val start =
        if (user != null) {
            Route.StartResolver
        } else Route.Login

    NavHost(
        navController = navController,
        startDestination = start,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {

        composable<Route.StartResolver> {
            StartResolverScreen(
                appLockStatus = appLockStatus,
                navigateToHome = { navController.navigate(Route.Home) { popUpTo(0) } },
                navigateToAppLock = { navController.navigate(Route.AppLock) { popUpTo(0) } },
                modifier = Modifier.padding(paddingValues)
            )
        }

        composable<Route.AppLock> {

            LaunchedEffect(Unit) {
                if (appLockStatus == true) {
                    biometricPromptManager.showBiometricPromptIfAvailable(
                        title = "Login using fingerprint",
                        description = "",
                        onSuccess = { navController.navigate(Route.Home) { popUpTo(0) } }
                    )
                }
            }

            AppLockScreen(
                correctPin = appLockPin,
                appLockStatus = appLockStatus!!,
                navigateUp = { navController.navigateUp() },
                navigateToHome = { navController.navigate(Route.Home) { popUpTo(0) } },
                onAction = appLockViewModel::onAction,
                onFingerprintClick = {
                    if (appLockStatus == true) {
                        biometricPromptManager.showBiometricPromptIfAvailable(
                            title = "Login using fingerprint",
                            description = "",
                            onSuccess = { navController.navigate(Route.Home) { popUpTo(0) } }
                        )
                    }
                }
            )
        }

        composable<Route.Login> {

            LoginScreen(
                modifier = Modifier.padding(paddingValues),
                onClick = {
                    loginViewModel.handleGoogleSignIn(
                        context,
                        navigateToHome = {
                            navController.navigate(Route.Home) {
                                popUpTo(Route.Login) { inclusive = true }
                            }
                        }
                    )
                }
            )
        }

        composable<Route.Home> {

            val state = homeViewModel.state.collectAsStateWithLifecycle().value

            val isDeletionInProgress = transactionInfoViewModel.isLoading.collectAsStateWithLifecycle().value

            var isTransactionInfoSheetOpen by remember { mutableStateOf(false) }

            HomeScreen(
                state = state,
                user = user,
                modifier = Modifier.padding(paddingValues),
                onAction = { homeViewModel.onAction(it) },
                onIncomeButtonClick = { navController.navigate(Route.AddTransaction(TransactionType.INCOME)) },
                onExpenseButtonClick = { navController.navigate(Route.AddTransaction(TransactionType.EXPENSE)) },
                isTransactionInfoSheetOpen = isTransactionInfoSheetOpen,
                toggleSheet = {isTransactionInfoSheetOpen = !isTransactionInfoSheetOpen},
                isDeletionInProgress = isDeletionInProgress,
                onDeleteTransaction = { transaction ->
                    transactionInfoViewModel.deleteTransaction(
                        context = context,
                        transaction = transaction,
                        onSuccess = { isTransactionInfoSheetOpen = false }
                    )
                }
            )
        }

        composable<Route.AddTransaction> {

            val transactionType = it.toRoute<Route.AddTransaction>().transactionType

            val addTransactionViewModel = koinViewModel<AddTransactionViewModel>()

            addTransactionViewModel.updateTransactionType(transactionType)

            val state = addTransactionViewModel.state.collectAsStateWithLifecycle().value

            AddTransactionScreen(
                state = state,
                onAction = { addTransactionViewModel.onAction(it) },
                onEvent = { addTransactionViewModel.onEvent(it) },
                event = addTransactionViewModel.event,
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

            val isDeletionInProgress = transactionInfoViewModel.isLoading.collectAsStateWithLifecycle().value

            var isTransactionInfoSheetOpen by remember { mutableStateOf(false) }

            HistoryScreen(
                modifier = Modifier.padding(paddingValues),
                state = state,
                user = user,
                onAction = { historyViewModel.onAction(it) },
                isTransactionInfoSheetOpen = isTransactionInfoSheetOpen,
                toggleSheet = {isTransactionInfoSheetOpen = !isTransactionInfoSheetOpen},
                isDeletionInProgress = isDeletionInProgress,
                onDeleteTransaction = { transaction ->
                    transactionInfoViewModel.deleteTransaction(
                        context = context,
                        transaction = transaction,
                        onSuccess = { isTransactionInfoSheetOpen = false }
                    )
                }
            )
        }

        composable<Route.Profile> {

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