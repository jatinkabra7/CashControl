package com.jk.cashcontrol.features.expense_tracker.presentation.add_transaction.components

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.jk.cashcontrol.R
import com.jk.cashcontrol.features.expense_tracker.domain.model.TransactionType
import com.jk.cashcontrol.features.expense_tracker.presentation.add_transaction.AddTransactionState

@Composable
fun AddTransactionTopBar(
    state: AddTransactionState,
    navigateToHome : () -> Unit,
    modifier: Modifier = Modifier
) {

    val topBarText =
        if(state.transactionType == TransactionType.INCOME) "Add Income"
        else "Add Expense"

    Row(
        modifier = modifier,
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

        Spacer(Modifier.width(dimensionResource(id = R.dimen.add_transaction_padding_medium)))

        Text(
            text = topBarText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Light,
            color = Color.White
        )
    }
}