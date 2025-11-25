package com.jk.cashcontrol.features.expense_tracker.presentation.statistics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.jk.cashcontrol.app.presentation.theme.CustomDarkOrange
import com.jk.cashcontrol.app.presentation.theme.CustomGreen
import com.jk.cashcontrol.app.presentation.theme.ForegroundColor
import com.jk.cashcontrol.features.expense_tracker.domain.model.CategoryWithAmount
import kotlin.math.exp

@Composable
fun IncomeExpenseBreakdown(
    incomeCategories: List<CategoryWithAmount>,
    expenseCategories: List<CategoryWithAmount>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if(expenseCategories.isNotEmpty()) {
                Table(
                    title = "Expense Breakdown",
                    transactionColor = CustomDarkOrange,
                    data = expenseCategories
                )
            }

            if(incomeCategories.isNotEmpty()) {
                Table(
                    title = "Income Breakdown",
                    transactionColor = CustomGreen,
                    data = incomeCategories
                )
            }
        }
    }
}

@Composable
fun Table(
    title: String,
    transactionColor: Color,
    data: List<CategoryWithAmount>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 5.dp)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = transactionColor.copy(0.2f),
                    shape = CircleShape
                )
                .padding(
                    horizontal = 10.dp,
                    vertical = 5.dp
                )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = transactionColor
            )
        }

        Spacer(Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .heightIn(max = 1000.dp)
                .background(ForegroundColor, RoundedCornerShape(20.dp))
                .padding(10.dp)
        ) {
            items(data.size * 2) { index ->

                val item = data[index / 2];
                val isNameCell = index % 2 == 0;

                if(isNameCell) {
                    Text(
                        text = item.name,
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "${item.amount}",
                        color = Color.White,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}