package com.jk.cashcontrol.presentation.statistics

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser
import com.jk.cashcontrol.R
import com.jk.cashcontrol.presentation.statistics.components.ThisMonthSection
import com.jk.cashcontrol.presentation.statistics.components.ThisYearSection
import com.jk.cashcontrol.presentation.statistics.components.TodaySection
import com.jk.cashcontrol.presentation.theme.PrimaryBlue
import kotlinx.coroutines.launch

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    state: StatisticsState,
    user: FirebaseUser?,
    onAction: (StatisticsAction) -> Unit
) {

    LaunchedEffect(user) {
        onAction(StatisticsAction.ReloadData)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
    ) {

        Text(
            text = "Statistics",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Light,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = dimensionResource(id = R.dimen.statistics_screen_horizontal_padding))
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.statistics_spacer_large)))

        val pagerState = rememberPagerState(pageCount = { 3 })

        val titles = listOf("Today", "This Month", "This Year")

        val scope = rememberCoroutineScope()

        ScrollableTabRow(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.statistics_screen_horizontal_padding)),
            edgePadding = 0.dp,
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            indicator = {},
            divider = {}
        ) {
            titles.forEachIndexed { index, title ->
                OutlinedButton(
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.statistics_screen_horizontal_padding))
                        .height(dimensionResource(id = R.dimen.statistics_screen_outlined_button_height)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (index == pagerState.currentPage) PrimaryBlue else Color.Transparent,
                        contentColor = Color.White
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (index == pagerState.currentPage) PrimaryBlue else Color.Gray.copy(
                            0.5f
                        )
                    ),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                index,
                                animationSpec = tween(
                                    easing = LinearOutSlowInEasing,
                                    durationMillis = 1000
                                )
                            )
                        }
                    }
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }

        Spacer(Modifier.height(dimensionResource(id = R.dimen.statistics_spacer_large)))

        HorizontalPager(
            pageSpacing = dimensionResource(id = R.dimen.statistics_screen_page_spacing),
            state = pagerState,
            modifier = Modifier
                .fillMaxHeight()
        ) { page ->

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                when (page) {
                    0 -> TodaySection(state = state, onAction = { onAction(it) })
                    1 -> ThisMonthSection(state = state, onAction = { onAction(it) })
                    2 -> ThisYearSection(state = state, onAction = { onAction(it) })
                }
            }
        }

        Spacer(Modifier.weight(1f))
    }

}