package com.jk.cashcontrol.presentation.statistics.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.Category
import com.jk.cashcontrol.presentation.statistics.StatisticsState

@Composable
fun ThisMonthSection(
    modifier: Modifier = Modifier,
    state : StatisticsState
) {

    val incomeCategories = listOf<Category>(
        Category(name = "Salary", icon = R.drawable.salary),
        Category(name = "Business", icon = R.drawable.business),
        Category(name = "Investment", icon = R.drawable.investment),
        Category(name = "Gifts", icon = R.drawable.gifts),
        Category(name = "Other", icon = R.drawable.other),
    )

    val expenseCategories = listOf<Category>(
        Category(name = "Food", icon = R.drawable.food),
        Category(name = "Business", icon = R.drawable.business),
        Category(name = "Movies", icon = R.drawable.entertainment),
        Category(name = "Transport", icon = R.drawable.transport),
        Category(name = "Shopping", icon = R.drawable.shopping),
        Category(name = "Bills", icon = R.drawable.bills),
        Category(name = "Education", icon = R.drawable.education),
        Category(name = "Other", icon = R.drawable.other),
    )

    val topIncomeCategoryIcon =incomeCategories.find { it.name == state.thisMonthTopIncomeCategory }!!.icon
    val topExpenseCategoryIcon =incomeCategories.find { it.name == state.thisMonthTopExpenseCategory }!!.icon

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color.Black)
            .padding(10.dp)
    ) {
        StatsCard(
            totalIncome = state.thisMonthIncome,
            totalExpense = state.thisMonthExpense
        )

        Spacer(Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = "Top Income - ${state.thisMonthTopIncomeCategory}",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Image(
                painter = painterResource(topIncomeCategoryIcon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
            )
        }

        Spacer(Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = "Top Expense - ${state.thisMonthTopExpenseCategory}",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Image(
                painter = painterResource(topExpenseCategoryIcon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
            )
        }

    }

}