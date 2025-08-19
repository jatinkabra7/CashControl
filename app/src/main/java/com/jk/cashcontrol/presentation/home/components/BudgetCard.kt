package com.jk.cashcontrol.presentation.home.components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.presentation.home.HomeActions
import com.jk.cashcontrol.presentation.theme.ButtonColor
import com.jk.cashcontrol.presentation.theme.ForegroundColor
import com.jk.cashcontrol.presentation.theme.ProgressBarColor
import com.jk.cashcontrol.presentation.theme.ProgressBarTrackColor

@Composable
fun BudgetCard(
    modifier: Modifier = Modifier,
    expense: Float,
    budget: Float,
    remaining: Float,
    onAction: (HomeActions) -> Unit
) {

    val progress = if (budget == 0f) 0f else expense / budget

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 500, easing = EaseInOut)
    )

    val animatedExpense by animateFloatAsState(
        targetValue = expense,
        animationSpec = tween(durationMillis = 1000, easing = EaseInOut)
    )

    val animatedBudget by animateFloatAsState(
        targetValue = budget,
        animationSpec = tween(durationMillis = 1000, easing = EaseInOut)
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.budget_card_corner_radius)))
            .background(ForegroundColor)
    ) {
        Spacer(Modifier.height(dimensionResource(id = R.dimen.budget_card_spacer_small)))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.budget_card_horizontal_padding))
        ) {
            Text(
                text = "Expense",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light,
                color = Color.White,
                fontFamily = FontFamily.SansSerif
            )

            Text(
                text = "Budget",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light,
                color = Color.White,
                fontFamily = FontFamily.SansSerif
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.budget_card_horizontal_padding))
        ) {
            Text(
                text = "%.2f".format(animatedExpense),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Normal
            )

            Text(
                text = "%.2f".format(animatedBudget),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(Modifier.height(dimensionResource(id = R.dimen.budget_card_spacer_small)))

        LinearProgressIndicator(
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = R.dimen.budget_card_horizontal_padding)
                )
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.budget_card_progress_height)),
            progress = { animatedProgress },
            color = ProgressBarColor,
            trackColor = ProgressBarTrackColor
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(id = R.dimen.budget_card_vertical_padding),
                    horizontal = dimensionResource(id = R.dimen.budget_card_horizontal_padding)
                )
        ) {
            Text(
                text = (progress * 100).toInt().toString() + "%",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 16.sp,
                color = Color.White.copy(0.8f),
                fontWeight = FontWeight.Thin
            )

            Text(
                text = "Remaining: $remaining",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 16.sp,
                color = Color.White.copy(0.8f),
                fontWeight = FontWeight.Thin
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = dimensionResource(id = R.dimen.budget_card_horizontal_padding))
        ) {

            // New Button
            ActionButton(
                title = "New Budget",
                textStyle = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Light,
                icon = Icons.Default.Add,
                onClick = { onAction(HomeActions.OnNewBudgetClick) },
                cornerRadius = dimensionResource(id = R.dimen.budget_card_button_corner_radius)
            )

            Spacer(Modifier.width(dimensionResource(id = R.dimen.budget_card_spacer_small)))

            // Edit Button
            ActionButton(
                title = "Edit Budget",
                textStyle = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Light,
                icon = Icons.Default.Edit,
                onClick = { onAction(HomeActions.OnEditBudgetClick) },
                cornerRadius = dimensionResource(id = R.dimen.budget_card_button_corner_radius)
            )
        }

        Spacer(Modifier.height(dimensionResource(id = R.dimen.budget_card_spacer_small)))
    }
}

@Composable
private fun ActionButton(
    title: String,
    textStyle: TextStyle,
    fontWeight: FontWeight,
    icon: ImageVector,
    cornerRadius: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconColor: Color = Color.White
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isButtonPressed by interactionSource.collectIsPressedAsState()

    var buttonScale = animateFloatAsState(
        targetValue = if(isButtonPressed) 0.9f else 1f,
        animationSpec = tween(durationMillis = 100, easing = LinearEasing)
    ).value

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .scale(buttonScale)
            .clickable(
                indication = null,
                interactionSource = interactionSource
            ) {
                onClick()
            }
            .background(
                shape = RoundedCornerShape(cornerRadius),
                color = ButtonColor
            )
            .padding(
                horizontal = dimensionResource(id = R.dimen.budget_card_button_horizontal_padding),
                vertical = dimensionResource(id = R.dimen.budget_card_button_vertical_padding)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(dimensionResource(id = R.dimen.budget_card_button_icon_size))
            )

            Spacer(Modifier.width(dimensionResource(id = R.dimen.budget_card_button_icon_text_spacing)))

            Text(
                text = title,
                color = Color.White,
                style = textStyle,
                fontWeight = fontWeight,
                maxLines = 1
            )
        }
    }
}
