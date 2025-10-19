package com.jk.cashcontrol.features.expense_tracker.presentation.statistics.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.app.presentation.theme.CustomLightBlue
import com.jk.cashcontrol.app.presentation.theme.CustomPink
import com.jk.cashcontrol.features.expense_tracker.presentation.statistics.StatisticsAction
import com.jk.cashcontrol.features.expense_tracker.presentation.statistics.TodayState

@Composable
fun TodaySection(
    state: TodayState,
    onAction: (StatisticsAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(horizontal = dimensionResource(id = R.dimen.statistics_screen_horizontal_padding))
    ) {
        StatsCard(
            totalIncome = state.todayIncome,
            totalExpense = state.todayExpense
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.statistics_spacer_large)))

        TopIncomeExpense(
            topIncomeCategory = state.todayTopIncomeCategory,
            topExpenseCategory = state.todayTopExpenseCategory
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.statistics_spacer_medium)))

        val isLoading = state.isTodaySummaryLoading
        val transition = rememberInfiniteTransition()

        val rotationAngle = transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(5000, easing = LinearEasing)
            )
        ).value

        val summaryButtonText =
            if (state.isTodaySummaryLoading) "Generating..."
            else "Generate Summary"

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (!state.isTodaySummaryLoading) {
                    onAction(StatisticsAction.OnTodayGenerateSummaryClick(state))
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            border = BorderStroke(1.dp, color = CustomLightBlue)
        ) {

            Image(
                painter = painterResource(R.drawable.gemini),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(if (isLoading) rotationAngle else 0f)
            )

            Spacer(Modifier.width(dimensionResource(id = R.dimen.statistics_spacer_medium)))

            Text(
                text = summaryButtonText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            CustomLightBlue,
                            CustomPink
                        )
                    )
                )
            )
        }

        Spacer(Modifier.height(dimensionResource(id = R.dimen.statistics_spacer_medium)))

        AnimatedVisibility(
            state.isTodaySummaryGenerated,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {

            Summary(state.todayGeneratedSummary)
        }

        Spacer(Modifier.weight(1f))
    }

}