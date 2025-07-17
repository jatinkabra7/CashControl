package com.jk.cashcontrol.presentation.transaction

import androidx.annotation.Keep
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.domain.model.TransactionType
import com.jk.cashcontrol.presentation.theme.CustomDarkGreen
import com.jk.cashcontrol.presentation.theme.CustomDarkOrange
import com.jk.cashcontrol.presentation.theme.CustomLightGreen
import com.jk.cashcontrol.presentation.theme.CustomLightOrange
import com.jk.cashcontrol.presentation.theme.CustomLightRed
import com.jk.cashcontrol.presentation.transaction.components.TransactionInfoTopBar

@Composable
@Keep
fun TransactionInfoScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    name : String,
    type : TransactionType,
    amount : Float,
    category: String,
    timestamp : String,
    timestampMillis: Long,
    onDeleteTransaction: (Transaction) -> Unit,
    navigateUp: () -> Unit
) {

    val transaction = Transaction(
        name = name,
        type = type,
        amount = amount,
        category = category,
        timestamp = timestamp,
        timestampMillis = timestampMillis
    )

    val transactionIcon =
        if (type == TransactionType.INCOME) R.drawable.baseline_arrow_downward_24
        else R.drawable.baseline_arrow_upward_24

    val transactionColor =
        if (type == TransactionType.INCOME) CustomLightGreen
        else CustomDarkOrange

    val brush =
        if(type == TransactionType.INCOME) {
            Brush.verticalGradient(listOf(CustomLightGreen, CustomDarkGreen))
        }
        else {
            Brush.verticalGradient(listOf(CustomLightOrange, CustomDarkOrange))
        }

    var isDeleteTransactionDialogVisible by rememberSaveable { mutableStateOf(false) }

    if(isDeleteTransactionDialogVisible) {
        AlertDialog(
            onDismissRequest = {isDeleteTransactionDialogVisible = false},
            title = {
                Text(
                    text = "Delete this transaction?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            confirmButton = {
                TextButton(
                    enabled = !isLoading,
                    onClick = {
                        onDeleteTransaction(transaction)
                    }
                ) {
                    Text(
                        text = if(!isLoading) "Delete" else "Deleting"
                    )
                }
            },
            dismissButton = {
                if(!isLoading) {
                    TextButton(
                        onClick = { isDeleteTransactionDialogVisible = false }
                    ) {
                        Text(
                            text = "Cancel"
                        )
                    }
                }

            }
        )
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        transactionColor.copy(0.5f),
                        Color.Black
                    )
                )
            )
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = 10.dp)
    ) {

        TransactionInfoTopBar(
            modifier = Modifier
                .align(Alignment.Start),
            navigateUp = navigateUp
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            IconButton(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(width = 10.dp, brush = brush, shape = CircleShape),
                onClick = {},
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.DarkGray.copy(0.5f)
                )
            ) {

                Icon(
                    painter = painterResource(transactionIcon),
                    contentDescription = null,
                    tint = transactionColor,
                    modifier = Modifier
                        .size(50.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "$amount",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = transactionColor
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = category,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = timestamp,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(Modifier.height(20.dp))

            DeleteTransactionButton(
                type = type,
                onClick = {
                    isDeleteTransactionDialogVisible = true
                }
            )

            Spacer(Modifier.height(200.dp))
        }
    }
}

@Composable
private fun DeleteTransactionButton(
    modifier: Modifier = Modifier,
    type: TransactionType,
    onClick: () -> Unit
) {

    val brush = Brush.linearGradient(
        if(type == TransactionType.EXPENSE) listOf(Color.Red, CustomDarkOrange, Color.Red)
        else listOf(CustomDarkGreen, CustomLightGreen, CustomDarkGreen)
    )

    Box(
        modifier = modifier
            .background(brush = brush, shape = RoundedCornerShape(100))
            .clickable {
                onClick()
            }
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_delete_24),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = "Delete Transaction",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

}