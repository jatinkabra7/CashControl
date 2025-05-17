package com.jk.cashcontrol.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.jk.cashcontrol.domain.model.User
import com.jk.cashcontrol.presentation.add_transaction.AddTransactionScreen
import com.jk.cashcontrol.presentation.add_transaction.AddTransactionViewModel
import com.jk.cashcontrol.presentation.home.HomeScreen
import com.jk.cashcontrol.presentation.home.HomeState
import com.jk.cashcontrol.presentation.home.HomeViewModel
import com.jk.cashcontrol.presentation.login.LoginScreen
import com.jk.cashcontrol.presentation.login.LoginViewModel
import com.jk.cashcontrol.presentation.profile.ProfileScreen
import org.koin.androidx.compose.koinViewModel
import kotlin.math.log

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {

    val user = Firebase.auth.currentUser

    val start =
        if(user!=null) {
            Route.Home
        } else Route.Login



    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = start
    ) {

        composable<Route.Home> {

            val viewModel = koinViewModel<HomeViewModel>()

            val state = viewModel.state.collectAsStateWithLifecycle().value

            HomeScreen(
                state = state,
                user = user
            )
        }

        composable<Route.AddTransaction> {

            val transactionType = it.toRoute<Route.AddTransaction>().transactionType

            val viewModel = koinViewModel<AddTransactionViewModel>()

            viewModel.updateTransactionType(transactionType)

            val state = viewModel.state.collectAsStateWithLifecycle().value

            AddTransactionScreen(
                state = state,
                onAction = {viewModel.onAction(it)},
                navigateToHome = {
                    navController.navigate(Route.Home) {
                        popUpTo(Route.AddTransaction(transactionType)){inclusive = true}
                    }
                }
            )
        }

        composable<Route.Statistics> {

        }

        composable<Route.History> {

        }

        composable<Route.Profile> {

            val appUser = if(user!=null) {
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
                        popUpTo(Route.Profile) {inclusive = true}
                    }
                }
            )
        }

        composable<Route.Login> {
            val loginViewModel = koinViewModel<LoginViewModel>()
            val context = LocalContext.current

            LoginScreen(onClick = {
                loginViewModel.handleGoogleSignIn(
                    context,
                    navigateToHome =  {
                        navController.navigate(Route.Home) {
                            popUpTo(Route.Login){inclusive = true}
                        }
                    }
                )
            })

        }
    }

}