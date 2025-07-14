package com.jk.cashcontrol.presentation.transaction.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R

@Composable
fun TransactionInfoTopBar(
    modifier: Modifier = Modifier,
    navigateUp : () -> Unit
) {

    val topBarText = "Transaction Info"

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {navigateUp()}
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