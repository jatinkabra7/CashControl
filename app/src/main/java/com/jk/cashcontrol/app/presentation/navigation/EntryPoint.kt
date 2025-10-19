package com.jk.cashcontrol.app.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.jk.cashcontrol.core.presentation.utils.Constants
import com.jk.cashcontrol.app.presentation.theme.BackgroundColor
import com.jk.cashcontrol.app.presentation.theme.CashControlTheme

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
            RootGraph(
                navController = navController,
                paddingValues = innerPadding,
                bannerAd = { BannerAd(adId = Constants.AD_ID) }
            )
        }
    }
}

@Composable
fun BannerAd(adId: String) {

    val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
        LocalContext.current,
        360
    )

    AndroidView(
        factory = { context ->
            AdView(context).apply {
                setAdSize(adSize)
                adUnitId = adId

                // Request an Ad
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}