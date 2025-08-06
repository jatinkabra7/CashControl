package com.jk.cashcontrol.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import com.jk.cashcontrol.domain.model.User
import com.jk.cashcontrol.presentation.add_transaction.AddTransactionScreen
import com.jk.cashcontrol.presentation.add_transaction.AddTransactionViewModel
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
import com.jk.cashcontrol.presentation.theme.CustomBlue
import com.jk.cashcontrol.presentation.theme.CustomDarkBlue
import com.jk.cashcontrol.presentation.transaction.TransactionInfoScreen
import com.jk.cashcontrol.presentation.transaction.TransactionInfoViewModel
import com.jk.cashcontrol.presentation.utils.fingerprint_login.BiometricPromptManager
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedSharedTransitionModifierParameter")
@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel = koinViewModel(),
    loginViewModel: LoginViewModel = koinViewModel(),
    historyViewModel: HistoryViewModel = koinViewModel(),
    statisticsViewModel: StatisticsViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel(),
    appLockViewModel: AppLockViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val activity = context as FragmentActivity

    val user = FirebaseAuth.getInstance().currentUser

    // fetching it here so it will be collected in the background before the composition of settings screen
    val appLockStatus = settingsViewModel.appLockStatus.collectAsStateWithLifecycle().value

    val appLockPin = appLockViewModel.appLockPin.collectAsStateWithLifecycle().value

    val biometricPromptManager = BiometricPromptManager(activity)

    val start =
        if (user != null) {
            Route.StartResolver
        } else Route.Login

    val gradient = Brush.linearGradient(
        colors = listOf(CustomBlue, CustomDarkBlue)
    )

    NavHost(
        navController = navController,
        startDestination = start,
        enterTransition = { EnterTransition.None },
        popEnterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popExitTransition = { ExitTransition.None }
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
                if(appLockStatus == true) {
                    biometricPromptManager.showBiometricPromptIfAvailable(
                        title = "Login using fingerprint",
                        description = "",
                        onSuccess = { navController.navigate(Route.Home) {popUpTo(0)} }
                    )
                }
            }


            AppLockScreen(
                correctPin = appLockPin,
                appLockStatus = appLockStatus!!,
                navigateUp = { navController.navigateUp() },
                navigateToHome = { navController.navigate(Route.Home) {popUpTo(0)} },
                onAction = appLockViewModel::onAction,
                onFingerprintClick = {
                    if(appLockStatus == true) {
                        biometricPromptManager.showBiometricPromptIfAvailable(
                            title = "Login using fingerprint",
                            description = "",
                            onSuccess = { navController.navigate(Route.Home) {popUpTo(0)} }
                        )
                    }
                },
                modifier = Modifier.padding(paddingValues)
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

            HomeScreen(
                state = state,
                user = user,
                gradient = gradient,
                modifier = Modifier.padding(paddingValues),
                onAction = { homeViewModel.onAction(it) },
                navigateToTransactionInfo = {
                    navController.navigate(
                        Route.TransactionInfo(
                            name = it.name,
                            type = it.type,
                            amount = it.amount,
                            category = it.category,
                            timestamp = it.timestamp,
                            timestampMillis = it.timestampMillis
                        )
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

            HistoryScreen(
                modifier = Modifier.padding(paddingValues),
                state = state,
                user = user,
                onAction = { historyViewModel.onAction(it) },
                navigateToTransactionInfo = {
                    navController.navigate(
                        Route.TransactionInfo(
                            name = it.name,
                            type = it.type,
                            amount = it.amount,
                            category = it.category,
                            timestamp = it.timestamp,
                            timestampMillis = it.timestampMillis
                        )
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

        composable<Route.TransactionInfo> {

            val transactionInfoViewModel: TransactionInfoViewModel = koinViewModel()

            val isLoading = transactionInfoViewModel.isLoading.collectAsStateWithLifecycle().value

            val name = it.toRoute<Route.TransactionInfo>().name
            val type = it.toRoute<Route.TransactionInfo>().type
            val amount = it.toRoute<Route.TransactionInfo>().amount
            val category = it.toRoute<Route.TransactionInfo>().category
            val timestamp = it.toRoute<Route.TransactionInfo>().timestamp
            val timestampMillis = it.toRoute<Route.TransactionInfo>().timestampMillis

            TransactionInfoScreen(
                modifier = Modifier,
                isLoading = isLoading,
                name = name,
                type = type,
                amount = amount,
                category = category,
                timestamp = timestamp,
                timestampMillis = timestampMillis,
                onDeleteTransaction = { transaction ->
                    transactionInfoViewModel.deleteTransaction(
                        context = context,
                        transaction = transaction,
                        navigateUp = { navController.navigateUp() }
                    )
                },
                navigateUp = { navController.navigateUp() }
            )
        }

        composable<Route.AppInfo> {
//            Text(
//                text = "App Version: 2.0",
//                style = MaterialTheme.typography.labelLarge,
//                color = Color.Gray,
//                modifier = Modifier.align(Alignment.CenterHorizontally),
//            )
        }
    }
}