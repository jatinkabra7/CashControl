package com.jk.cashcontrol.presentation.add_transaction.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.TransactionType
import com.jk.cashcontrol.presentation.add_transaction.AddTransactionState

@Composable
fun AddTransactionTopBar(
    modifier: Modifier = Modifier,
    state: AddTransactionState,
    navigateToHome : () -> Unit
) {

    val topBarText =
        if(state.transactionType == TransactionType.INCOME) "Add Income"
        else "Add Expense"

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {navigateToHome()}
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_arrow_back_24),
                contentDescription = null,
                tint = Color.White
            )
        }

        Spacer(Modifier.width(20.dp))

        Text(
            text = topBarText,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontSize = 20.sp
        )
    }
}