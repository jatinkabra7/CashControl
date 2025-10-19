package com.jk.cashcontrol.app.presentation.navigation

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jk.cashcontrol.features.auth.data.fingerprint_login.BiometricPromptManager
import com.jk.cashcontrol.features.auth.domain.model.User
import com.jk.cashcontrol.features.auth.presentation.app_lock.AppLockScreen
import com.jk.cashcontrol.features.auth.presentation.app_lock.AppLockViewModel
import com.jk.cashcontrol.features.auth.presentation.login.LoginScreen
import com.jk.cashcontrol.features.auth.presentation.login.LoginViewModel
import com.jk.cashcontrol.features.auth.presentation.start_resolver.StartResolverScreen
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    start: Route,
    context: Context,
    appLockStatus: Boolean?,
    appLockPin: String,
    loginViewModel: LoginViewModel,
    appLockViewModel: AppLockViewModel,
) {
    val activity = context as FragmentActivity

    val biometricPromptManager = BiometricPromptManager(activity)

    navigation<NavigationGraphs.AuthGraph>(startDestination = start) {
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
                    if (appLockStatus) {
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
    }
}