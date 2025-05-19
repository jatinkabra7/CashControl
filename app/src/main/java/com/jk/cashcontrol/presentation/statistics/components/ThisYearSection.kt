package com.jk.cashcontrol.presentation.statistics.components

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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.Category
import com.jk.cashcontrol.presentation.statistics.StatisticsAction
import com.jk.cashcontrol.presentation.statistics.StatisticsState
import com.jk.cashcontrol.presentation.theme.CustomLightBlue
import com.jk.cashcontrol.presentation.theme.CustomPink

@Composable
fun ThisYearSection(
    modifier: Modifier = Modifier,
    state : StatisticsState,
    onAction : (StatisticsAction) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color.Black)
            .padding(10.dp)
            .verticalScroll(scrollState)
    ) {
        StatsCard(
            totalIncome = state.thisYearIncome,
            totalExpense = state.thisYearExpense
        )

        Spacer(Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = "Top Income - ${state.thisYearTopIncomeCategory}",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = "Top Expense - ${state.thisYearTopExpenseCategory}",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(10.dp))

        val isLoading = state.isThisYearSummaryLoading
        val transition = rememberInfiniteTransition()

        val rotationAngle = transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(5000, easing = LinearEasing)
            )
        ).value

        OutlinedButton(
            onClick = {onAction(StatisticsAction.OnThisYearGenerateSummaryClick(state))},
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
                    .rotate(if(isLoading) rotationAngle else 0f),

                )

            Spacer(Modifier.width(10.dp))

            Text(
                text = "Generate Summary",
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

        Spacer(Modifier.height(10.dp))

        AnimatedVisibility(
            state.isThisYearSummaryGenerated,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(shape = RoundedCornerShape(10), color = Color.DarkGray.copy(0.5f))
                    .padding(10.dp)
            ) {
                Text(
                    text = state.thisYearGeneratedSummary,
                    fontSize = 14.sp,
                    color = Color.White.copy(0.8f)
                )
            }
        }

        Spacer(Modifier.weight(1f))

    }

}