package com.jk.cashcontrol.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
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
import com.jk.cashcontrol.presentation.statistics.StatisticsState
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel = koinViewModel<HomeViewModel>(),
    loginViewModel: LoginViewModel = koinViewModel<LoginViewModel>(),
    historyViewModel: HistoryViewModel = koinViewModel<HistoryViewModel>()
) {

    val user = Firebase.auth.currentUser

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
                onAction = { homeViewModel.onAction(it) }
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

            StatisticsScreen(modifier = Modifier.padding(paddingValues), state = StatisticsState())
        }

        composable<Route.History> {

            val state = historyViewModel.state.collectAsStateWithLifecycle().value

            HistoryScreen(
                modifier = Modifier.padding(paddingValues),
                state = state,
                user = user,
                onAction = { historyViewModel.onAction(it) }
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

            ProfileScreen(
                user = appUser,
                onLogout = {
                    Firebase.auth.signOut()
                    navController.navigate(Route.Login) {
                        popUpTo(Route.Profile) { inclusive = true }
                    }
                },
                modifier = Modifier.padding(paddingValues)
            )
        }

        composable<Route.Login> {
            val context = LocalContext.current

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