package com.jk.cashcontrol.presentation.home.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.presentation.home.HomeAction
import com.jk.cashcontrol.presentation.theme.CustomLightRed
import com.jk.cashcontrol.presentation.theme.CustomPink
import com.jk.cashcontrol.presentation.theme.CustomPurple

@Composable
fun BudgetCard(
    modifier: Modifier = Modifier,
    expense : Float,
    budget : Float,
    remaining : Float,
    onAction : (HomeAction) -> Unit
) {

    val progress = if (budget == 0f) 0f else expense / budget

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f,1f),
        animationSpec = tween(durationMillis = 500, easing = EaseInOut)
    )

    val animatedExpense by animateFloatAsState(
        targetValue = expense,
        animationSpec = tween(durationMillis = 1000, easing = EaseInOut)
    )

    val animatedBudget by animateFloatAsState(
        targetValue = budget,
        animationSpec = tween(durationMillis = 1000,easing = EaseInOut)
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(colors = listOf(CustomLightRed, CustomPink, CustomPurple)))

    ) {
        Spacer(Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {

            Text(
                modifier = Modifier,
                text = "Expense",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontFamily = FontFamily.SansSerif
            )

            Text(
                text = "Budget",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontFamily = FontFamily.SansSerif

            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {

            Text(
                text = "%.2f".format(animatedExpense),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = Bold
            )

            Text(
                text = "%.2f".format(animatedBudget),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = Bold
            )

        }

        Spacer(Modifier.height(10.dp))

        LinearProgressIndicator(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .height(6.dp),
            progress = { animatedProgress },
            color = Color.White.copy(0.9f),
            trackColor = Color.White.copy(0.5f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp)
        ) {

            Text(
                text = (progress*100).toInt().toString() + "%",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 16.sp,
                color = Color.White.copy(0.8f),
                fontWeight = FontWeight.Normal
            )

            Text(
                text = "Remaining: $remaining",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 16.sp,
                color = Color.White.copy(0.8f),
                fontWeight = FontWeight.Normal
            )
        }


        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 10.dp)
        ) {

            // New Button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) {
                        onAction(HomeAction.OnNewBudgetClick)
                    }
                    .background(shape = RoundedCornerShape(20.dp), color = Color.White.copy(0.4f))
                    .padding(10.dp)


            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                    )

                    Spacer(Modifier.width(10.dp))

                    Text(
                        text = "New Budget",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(Modifier.width(10.dp))

            // edit button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) {
                        onAction(HomeAction.OnEditBudgetClick)
                    }
                    .background(shape = RoundedCornerShape(20.dp), color = Color.White.copy(0.4f))
                    .padding(10.dp)


            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,

                    ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                    )

                    Spacer(Modifier.width(10.dp))

                    Text(
                        text = "Edit Budget",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

        }

        Spacer(Modifier.height(10.dp))

    }
}