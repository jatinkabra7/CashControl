package com.jk.cashcontrol.presentation.statistics

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser
import com.jk.cashcontrol.presentation.statistics.components.ThisMonthSection
import com.jk.cashcontrol.presentation.statistics.components.ThisYearSection
import com.jk.cashcontrol.presentation.statistics.components.TodaySection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    state: StatisticsState,
    user : FirebaseUser?,
    onAction: (StatisticsAction) -> Unit
) {

    LaunchedEffect(user) {
        onAction(StatisticsAction.ReloadData)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(10.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        Text(
            text = "Statistics",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Start)
        )

        Spacer(Modifier.height(20.dp))

        val pagerState = rememberPagerState(pageCount = {3})

        val titles = listOf("Today", "This Month", "This Year")

        val scope = rememberCoroutineScope()

        ScrollableTabRow (
            edgePadding = 0.dp,
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            indicator = {},
            divider = {}
        ) {
           titles.forEachIndexed { index, title ->
               OutlinedButton(
                   modifier = Modifier.padding(end = 8.dp),
                   colors = ButtonDefaults.buttonColors(
                       containerColor = if(index == pagerState.currentPage) Color.Gray.copy(0.5f) else Color.Transparent,
                       contentColor = Color.White
                   ),
                   onClick =  {
                       scope.launch {
                           pagerState.animateScrollToPage(
                               index,
                               animationSpec = tween(easing = LinearEasing, durationMillis = 300)
                           )
                       }
                   }
               ) {
                   Text(
                       text = title
                   )
               }
           }
        }

        Spacer(Modifier.height(20.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxHeight()
        ) {page ->

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                when(page) {
                    0 -> TodaySection(state = state, onAction = {onAction(it)})
                    1 -> ThisMonthSection(state = state, onAction = {onAction(it)})
                    2 -> ThisYearSection(state = state, onAction = {onAction(it)})
                }
            }


        }

        Spacer(Modifier.weight(1f))
    }

}