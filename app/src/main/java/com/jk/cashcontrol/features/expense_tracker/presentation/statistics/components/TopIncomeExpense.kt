package com.jk.cashcontrol.features.expense_tracker.presentation.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.jk.cashcontrol.R

@Composable
fun TopIncomeExpense(
    topIncomeCategory: String,
    topExpenseCategory: String
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Text(
            text = "Top Income - $topIncomeCategory",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )
    }

    Spacer(Modifier.height(dimensionResource(id = R.dimen.statistics_spacer_medium)))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Text(
            text = "Top Expense - $topExpenseCategory",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )
    }

}