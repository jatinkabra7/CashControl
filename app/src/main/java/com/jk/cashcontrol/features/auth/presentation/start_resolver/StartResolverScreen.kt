package com.jk.cashcontrol.features.auth.presentation.start_resolver

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun StartResolverScreen(
    navigateToHome: () -> Unit,
    navigateToAppLock: () -> Unit,
    appLockStatus: Boolean?,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(appLockStatus) {
        if (appLockStatus == null) return@LaunchedEffect
        if (appLockStatus) navigateToAppLock()
        else navigateToHome()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator()
    }

}