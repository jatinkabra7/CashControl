package com.jk.cashcontrol.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.domain.model.TransactionType
import com.jk.cashcontrol.presentation.theme.CustomDarkOrange

@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onClick: (Transaction) -> Unit
) {

    val transactionIcon =
        if (transaction.type == TransactionType.INCOME) R.drawable.baseline_arrow_downward_24
        else R.drawable.baseline_arrow_upward_24

    val transactionColor =
        if (transaction.type == TransactionType.INCOME) Color.Green
        else CustomDarkOrange


    Row(
        modifier = modifier
            .clip(RoundedCornerShape(100))
            .fillMaxWidth()
            .background(Color.DarkGray.copy(0.4f))
            .clickable { onClick(transaction) }

    ) {

        Spacer(Modifier.width(8.dp))

        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(40.dp),
            onClick = {onClick(transaction)},
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.DarkGray.copy(0.5f)
            )
        ) {

            Icon(
                painter = painterResource(transactionIcon),
                contentDescription = null,
                tint = transactionColor,
                modifier = Modifier
                    .size(30.dp)
            )
        }

        Spacer(Modifier.width(20.dp))


        Column(
            modifier = Modifier
                .widthIn(max = 180.dp)
                .padding(vertical = 5.dp)
        ) {

            Text(
                text = transaction.name,
                style = MaterialTheme.typography.titleMedium,
                color = transactionColor,
                fontSize = 20.sp,
                maxLines = 1,
                modifier = Modifier
                    .basicMarquee()

            )

            Text(
                text = transaction.timestamp + " - " + transaction.category,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(0.7f),
                fontSize = 16.sp,
                modifier = Modifier
                    .basicMarquee()
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = transaction.amount.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = transactionColor,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 10.dp)
        )


    }
}