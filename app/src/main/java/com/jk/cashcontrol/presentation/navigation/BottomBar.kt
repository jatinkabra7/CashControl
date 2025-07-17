package com.jk.cashcontrol.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jk.cashcontrol.R

@Composable
fun BottomBar(
    show : Boolean,
    onClick : (Route) -> Unit,
    currentRoute : String?
) {

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
            containerColor = Color.Black
        ) {

            list.forEach {

                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Transparent,
                        indicatorColor = Color.DarkGray.copy(0.7f),
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
                                Icon(
                                    painter = painterResource(R.drawable.history_outlined),
                                    contentDescription = null,
                                    tint = Color.White.copy(),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Route.Home -> {
                                Icon(
                                    painter = painterResource(R.drawable.home_outlined),
                                    contentDescription = null,
                                    tint = Color.White.copy(),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Route.Profile -> {
                                Icon(
                                    painter = painterResource(R.drawable.user),
                                    contentDescription = null,
                                    tint = Color.White.copy(),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Route.Statistics -> {
                                Icon(
                                    painter = painterResource(R.drawable.statistics_outlined),
                                    contentDescription = null,
                                    tint = Color.White.copy(),
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            else -> {}
                        }
                    }
                )
            }
        }
    }
}