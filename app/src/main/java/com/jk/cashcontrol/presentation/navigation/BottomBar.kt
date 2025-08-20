package com.jk.cashcontrol.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.jk.cashcontrol.R
import com.jk.cashcontrol.presentation.theme.ForegroundColor
import com.jk.cashcontrol.presentation.theme.PrimaryBlue

@Composable
fun BottomBar(
    show: Boolean,
    onClick: (Route) -> Unit,
    currentRoute: String?
) {

    val list = listOf<Route>(
        Route.Home, Route.Statistics, Route.History, Route.Profile
    )

    AnimatedVisibility(
        show,
        exit = fadeOut() + slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 250, easing = LinearEasing)
        ),
        enter = fadeIn() + slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 250, easing = LinearEasing)
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.bottom_bar_horizontal_padding),
                    vertical = dimensionResource(id = R.dimen.bottom_bar_vertical_padding)
                )
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.bottom_bar_corner_radius)))
                .background(ForegroundColor)
                .height(dimensionResource(id = R.dimen.bottom_bar_height))
        ) {
            list.forEach {
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = PrimaryBlue
                    ),
                    selected = currentRoute?.contains(it::class.simpleName.toString()) == true,
                    onClick = {
                        if (currentRoute?.contains(it::class.simpleName.toString()) == false) {
                            onClick(it)
                        }
                    },
                    icon = {
                        when (it) {
                            Route.History -> {
                                Icon(
                                    painter = painterResource(R.drawable.history_outlined),
                                    contentDescription = null,
                                    tint = Color.White.copy(),
                                    modifier = Modifier.size(dimensionResource(id = R.dimen.bottom_bar_icon_size))
                                )
                            }

                            Route.Home -> {
                                Icon(
                                    painter = painterResource(R.drawable.home_outlined),
                                    contentDescription = null,
                                    tint = Color.White.copy(),
                                    modifier = Modifier.size(dimensionResource(id = R.dimen.bottom_bar_icon_size))
                                )
                            }

                            Route.Profile -> {
                                Icon(
                                    painter = painterResource(R.drawable.user),
                                    contentDescription = null,
                                    tint = Color.White.copy(),
                                    modifier = Modifier.size(dimensionResource(id = R.dimen.bottom_bar_icon_size))
                                )
                            }

                            Route.Statistics -> {
                                Icon(
                                    painter = painterResource(R.drawable.statistics_outlined),
                                    contentDescription = null,
                                    tint = Color.White.copy(),
                                    modifier = Modifier.size(dimensionResource(id = R.dimen.bottom_bar_icon_size))
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
