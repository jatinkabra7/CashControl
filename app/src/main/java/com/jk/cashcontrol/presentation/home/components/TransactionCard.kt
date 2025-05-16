package com.jk.cashcontrol.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.domain.model.TransactionType
import java.nio.file.WatchEvent

@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    transaction : Transaction
) {

    val transactionIcon =
        if(transaction.type == TransactionType.INCOME) R.drawable.baseline_arrow_downward_24
        else R.drawable.baseline_arrow_upward_24

    val transactionColor =
        if(transaction.type == TransactionType.INCOME) Color.Green
        else Color.Red


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {

        IconButton(
            onClick = {},
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.DarkGray.copy(0.5f)
            )
        ) {

            Icon(
                painter = painterResource(transactionIcon),
                contentDescription = null,
                tint = transactionColor,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(Modifier.width(20.dp))


        Column(
            modifier = Modifier
                .widthIn(max = 180.dp)
        ) {

            Text(
                text = transaction.name,
                style = MaterialTheme.typography.titleMedium,
                color = transactionColor.copy(0.8f),
                fontSize = 20.sp,
                modifier = Modifier
                    .basicMarquee()

            )

            Text(
                text = transaction.timestamp + " - " + transaction.category,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(0.7f),
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = transaction.amount.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = transactionColor,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterVertically)
        )


    }
}

@Preview
@Composable
private fun p() {
    val transaction = Transaction(
        timestamp = "12th may 2025",
        category = "Food",
        name = "New Transaction New Transaction New Transaction",
        type = TransactionType.INCOME,
        amount = 20f
    )
    TransactionCard(
        transaction = transaction
    )
}