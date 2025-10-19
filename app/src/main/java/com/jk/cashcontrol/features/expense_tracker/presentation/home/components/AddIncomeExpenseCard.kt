package com.jk.cashcontrol.features.expense_tracker.presentation.home.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.app.presentation.theme.ButtonColor
import com.jk.cashcontrol.app.presentation.theme.CustomDarkOrange
import com.jk.cashcontrol.app.presentation.theme.CustomGreen
import com.jk.cashcontrol.app.presentation.theme.ForegroundColor

@Composable
fun AddIncomeExpenseCard(
    onIncomeButtonClick: () -> Unit,
    onExpenseButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val expenseColor = CustomDarkOrange

    val incomeColor = CustomGreen

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.budget_card_corner_radius)))
            .background(ForegroundColor)
            .padding(
                horizontal = dimensionResource(R.dimen.income_expense_card_horizontal_padding),
                vertical = dimensionResource(R.dimen.income_expense_card_vertical_padding)
            )
    ) {

        Row {

            // Add Income Button
            ActionButton(
                title = "Income",
                textStyle = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                icon = ImageVector.vectorResource(R.drawable.down_left_arrow),
                iconColor = incomeColor,
                onClick = { onIncomeButtonClick() },
                topStartCornerRadius = dimensionResource(id = R.dimen.income_expense_card_button_radius),
                bottomStartCornerRadius = dimensionResource(id = R.dimen.income_expense_card_button_radius),
                topEndCornerRadius = dimensionResource(id = R.dimen.income_expense_card_button_radius_small),
                bottomEndCornerRadius = dimensionResource(id = R.dimen.income_expense_card_button_radius_small),
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
            )

            Spacer(Modifier.width(dimensionResource(id = R.dimen.income_expense_card_spacer)))

            // Add Expense Button
            ActionButton(
                title = "Expense",
                textStyle = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                icon = ImageVector.vectorResource(R.drawable.top_right_arrow),
                iconColor = expenseColor,
                onClick = { onExpenseButtonClick() },
                topStartCornerRadius = dimensionResource(id = R.dimen.income_expense_card_button_radius_small),
                bottomStartCornerRadius = dimensionResource(id = R.dimen.income_expense_card_button_radius_small),
                topEndCornerRadius = dimensionResource(id = R.dimen.income_expense_card_button_radius),
                bottomEndCornerRadius = dimensionResource(id = R.dimen.income_expense_card_button_radius),
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun ActionButton(
    title: String,
    textStyle: TextStyle,
    fontWeight: FontWeight,
    icon: ImageVector,
    topStartCornerRadius: Dp,
    topEndCornerRadius: Dp,
    bottomStartCornerRadius: Dp,
    bottomEndCornerRadius: Dp,
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

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .scale(buttonScale)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .background(
                shape = RoundedCornerShape(
                    topStart = topStartCornerRadius,
                    topEnd = topEndCornerRadius,
                    bottomStart = bottomStartCornerRadius,
                    bottomEnd = bottomEndCornerRadius
                ),
                color = ButtonColor
            )
            .padding(
                horizontal = dimensionResource(id = R.dimen.income_expense_card_horizontal_padding),
                vertical = dimensionResource(id = R.dimen.income_expense_card_vertical_padding)
            )
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