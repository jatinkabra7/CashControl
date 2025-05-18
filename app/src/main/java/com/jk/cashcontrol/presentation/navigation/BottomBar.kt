package com.jk.cashcontrol.presentation.navigation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jk.cashcontrol.R

@Composable
fun BottomBar(
    show : Boolean,
    modifier: Modifier = Modifier,
    onClick : (Route) -> Unit,
    currentRoute : String?
) {

    Log.d("currentRoute",currentRoute.toString())

    val list = listOf<Route>(
        Route.Home, Route.Statistics, Route.AddTransactionEntry, Route.History, Route.Profile
    )

    AnimatedVisibility(
        show,
        exit = fadeOut() + slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 500, easing = LinearEasing)
        ),
        enter = fadeIn() + slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 500, easing = LinearEasing)
        )
    ) {

        BottomAppBar(
            containerColor = Color.Black,
            modifier = Modifier.height(60.dp)
        ) {

            list.forEach {

                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Transparent,
                        indicatorColor = Color.Transparent,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Transparent
                    ),
                    label = {
                        if(it != Route.AddTransactionEntry) {
                            Text(
                                it.toString(),
                                color = Color.White
                            )
                        }
                        else {
                            Text(text = "")
                        }
                    },
                    selected = currentRoute?.contains(it::class.simpleName.toString()) == true,
                    onClick = {
                        if(currentRoute?.contains(it::class.simpleName.toString()) == false) {
                            onClick(it)
                        }
                    },
                    icon = {
                        when(it) {
                            Route.AddTransactionEntry -> {
                                Image(
                                    painter = painterResource(R.drawable.plus),
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            Route.History -> {
                                if(currentRoute?.contains(it::class.simpleName.toString()) == true) {
                                    Icon(
                                        painter = painterResource(R.drawable.history_filled),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(R.drawable.history_outlined),
                                        contentDescription = null,
                                        tint = Color.White.copy(),
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                            Route.Home -> {
                                if(currentRoute?.contains(it::class.simpleName.toString()) == true) {
                                    Icon(
                                        painter = painterResource(R.drawable.home_filled),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(R.drawable.home_outlined),
                                        contentDescription = null,
                                        tint = Color.White.copy(),
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                            Route.Profile -> {
                                if(currentRoute?.contains(it::class.simpleName.toString()) == true) {
                                    Icon(
                                        painter = painterResource(R.drawable.profile_filled),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(R.drawable.user),
                                        contentDescription = null,
                                        tint = Color.White.copy(),
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                            Route.Statistics -> {
                                if(currentRoute?.contains(it::class.simpleName.toString()) == true) {
                                    Icon(
                                        painter = painterResource(R.drawable.statistics_filled),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(R.drawable.statistics_outlined),
                                        contentDescription = null,
                                        tint = Color.White.copy(),
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }

                            else -> {}
                        }
                    }
                )
            }
        }
    }


}