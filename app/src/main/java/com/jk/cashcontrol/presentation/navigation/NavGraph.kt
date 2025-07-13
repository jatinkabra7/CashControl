package com.jk.cashcontrol.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import com.jk.cashcontrol.domain.model.User
import com.jk.cashcontrol.presentation.add_transaction.AddTransactionScreen
import com.jk.cashcontrol.presentation.add_transaction.AddTransactionViewModel
import com.jk.cashcontrol.presentation.history.HistoryScreen
import com.jk.cashcontrol.presentation.history.HistoryViewModel
import com.jk.cashcontrol.presentation.home.HomeScreen
import com.jk.cashcontrol.presentation.home.HomeViewModel
import com.jk.cashcontrol.presentation.login.LoginScreen
import com.jk.cashcontrol.presentation.login.LoginViewModel
import com.jk.cashcontrol.presentation.profile.ProfileScreen
import com.jk.cashcontrol.presentation.statistics.StatisticsScreen
import com.jk.cashcontrol.presentation.statistics.StatisticsViewModel
import com.jk.cashcontrol.presentation.transaction.TransactionInfoScreen
import com.jk.cashcontrol.presentation.transaction.TransactionInfoViewModel
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
    statisticsViewModel: StatisticsViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val user = FirebaseAuth.getInstance().currentUser

    val start =
        if (user != null) {
            Route.Home
        } else Route.Login

    NavHost(
        navController = navController,
        startDestination = start,
        enterTransition = { EnterTransition.None },
        popEnterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {

        composable<Route.Home> {

            val state = homeViewModel.state.collectAsStateWithLifecycle().value

            HomeScreen(
                state = state,
                user = user,
                modifier = Modifier.padding(paddingValues),
                onAction = { homeViewModel.onAction(it) },
                navigateToTransactionInfo = {
                    navController.navigate(Route.TransactionInfo(
                        name = it.name,
                        type = it.type,
                        amount = it.amount,
                        category = it.category,
                        timestamp = it.timestamp,
                        timestampMillis = it.timestampMillis
                    ))
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
                onAction = {statisticsViewModel.onAction(it)}
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
                    navController.navigate(Route.TransactionInfo(
                        name = it.name,
                        type = it.type,
                        amount = it.amount,
                        category = it.category,
                        timestamp = it.timestamp,
                        timestampMillis = it.timestampMillis
                    ))
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

            val isDeleting = loginViewModel.isDeleting.collectAsStateWithLifecycle().value

            ProfileScreen(
                modifier = Modifier.padding(paddingValues),
                user = appUser,
                onLogout = {
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
                isDeleting = isDeleting
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
                        navigateUp = {navController.navigateUp()}
                    )
                },
                navigateUp = {navController.navigateUp()}
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
    }

}