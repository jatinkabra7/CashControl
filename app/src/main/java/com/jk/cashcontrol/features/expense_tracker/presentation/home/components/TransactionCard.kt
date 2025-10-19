package com.jk.cashcontrol.features.expense_tracker.presentation.home.components

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.jk.cashcontrol.R
import com.jk.cashcontrol.features.expense_tracker.domain.model.Transaction
import com.jk.cashcontrol.features.expense_tracker.domain.model.TransactionType
import com.jk.cashcontrol.app.presentation.theme.ButtonColor
import com.jk.cashcontrol.app.presentation.theme.CustomDarkOrange
import com.jk.cashcontrol.app.presentation.theme.CustomGreen

@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onClick: (Transaction) -> Unit
) {

    val transactionIcon =
        if (transaction.type == TransactionType.INCOME) R.drawable.down_left_arrow
        else R.drawable.top_right_arrow

    val transactionColor =
        if (transaction.type == TransactionType.INCOME) CustomGreen
        else CustomDarkOrange

    val plusOrMinus =
        if (transaction.type == TransactionType.INCOME) '+'
        else '-'


    Row(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.transaction_card_corner_radius)))
            .fillMaxWidth()
            .clickable { onClick(transaction) }
    ) {

        Spacer(Modifier.width(dimensionResource(id = R.dimen.transaction_card_horizontal_padding)))

        IconButton(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(dimensionResource(id = R.dimen.transaction_card_icon_button_size)),
            onClick = { onClick(transaction) },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = ButtonColor
            )
        ) {
            Icon(
                painter = painterResource(transactionIcon),
                contentDescription = null,
                tint = transactionColor,
                modifier = Modifier.size(dimensionResource(id = R.dimen.transaction_card_icon_size))
            )
        }

        Spacer(Modifier.width(dimensionResource(id = R.dimen.transaction_card_icon_text_spacing)))

        Column(
            modifier = Modifier
                .widthIn(max = dimensionResource(id = R.dimen.transaction_card_text_max_width))
                .padding(vertical = dimensionResource(id = R.dimen.transaction_card_text_vertical_padding))
        ) {
            Text(
                text = transaction.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )

            Text(
                text = transaction.timestamp + " - " + transaction.category,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(0.7f),
                modifier = Modifier.basicMarquee()
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = plusOrMinus + transaction.amount.toString(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = if (transaction.type == TransactionType.INCOME) transactionColor else Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = dimensionResource(id = R.dimen.transaction_card_amount_end_padding))
        )
    }

}