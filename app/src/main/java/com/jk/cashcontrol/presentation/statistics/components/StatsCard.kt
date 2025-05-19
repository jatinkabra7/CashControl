package com.jk.cashcontrol.presentation.statistics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import kotlin.math.exp

@Composable
fun StatsCard(
    modifier: Modifier = Modifier,
    totalIncome : Float,
    totalExpense : Float
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.DarkGray.copy(0.5f), shape = RoundedCornerShape(20))
        ,

    ) {

        FlowRow(
            horizontalArrangement = Arrangement.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .widthIn(max = 300.dp)
        ) {
            IncomeItem(income = totalIncome, modifier = Modifier.padding(bottom = 10.dp))

            Spacer(Modifier.width(20.dp))

            ExpenseItem(expense = totalExpense)
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Net Balance : ${totalIncome-totalExpense}",
            color = Color.White.copy(0.5f),
            fontSize = 14.sp
        )

    }
}

@Composable
private fun IncomeItem(
    modifier: Modifier = Modifier,
    income : Float
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {},
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.DarkGray.copy(0.7f)
            ),
            modifier = Modifier
                .size(40.dp)
        ) {

            Icon(
                painter = painterResource(R.drawable.baseline_arrow_downward_24),
                contentDescription = null,
                tint = Color.Green,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(Modifier.width(20.dp))

        Text(
            text = "$income",
            color = Color.Green,
            fontSize = 30.sp
        )
    }
}

@Composable
private fun ExpenseItem(
    modifier: Modifier = Modifier,
    expense : Float
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {},
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.DarkGray.copy(0.7f)
            ),
            modifier = Modifier
                .size(40.dp)
        ) {

            Icon(
                painter = painterResource(R.drawable.baseline_arrow_upward_24),
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(Modifier.width(20.dp))

        Text(
            text = "${expense}",
            color = Color.Red,
            fontSize = 30.sp
        )
    }
}

@Preview
@Composable
private fun p() {

    StatsCard(
        totalIncome = 50f,
        totalExpense = 50f
    )

}