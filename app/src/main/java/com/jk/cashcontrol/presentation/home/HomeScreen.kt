package com.jk.cashcontrol.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.presentation.home.components.BudgetCard
import com.jk.cashcontrol.presentation.home.components.TransactionCard
import com.jk.cashcontrol.presentation.theme.CustomBlue
import com.jk.cashcontrol.presentation.theme.CustomDarkBlue

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    gradient: Brush,
    user: FirebaseUser?,
    onAction: (HomeAction) -> Unit,
    navigateToTransactionInfo: (Transaction) -> Unit
) {

    LaunchedEffect(user?.uid.toString()) {
        onAction(HomeAction.ReloadData)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(10.dp)
    ) {

        HeaderSection(
            state = state,
            gradient = gradient,
            username = user?.displayName.toString(),
            onAction = { onAction(it) }
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Recent Transactions",
            style = TextStyle(gradient),
            fontSize = 24.sp,
            color = Color.White
        )

        Spacer(Modifier.height(10.dp))

        RecentTransactions(
            state = state,
            navigateToTransactionInfo = navigateToTransactionInfo
        )


    }
}

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
    gradient: Brush,
    state: HomeState,
    username: String = "Username",
    onAction: (HomeAction) -> Unit
) {
    Column(modifier = modifier) {
        Spacer(Modifier.height(10.dp))

        Text(
            text = "Hello",
            color = Color.White.copy(0.8f),
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                text = if (username.length <= 16) username.take(16) else username.take(16) + "...",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge
            )
        }


        Spacer(Modifier.height(10.dp))

        BudgetCard(
            gradient = gradient,
            expense = state.expense,
            budget = state.budget,
            remaining = state.remaining,
            onAction = { onAction(it) }
        )
    }
}

@Composable
fun RecentTransactions(
    modifier: Modifier = Modifier,
    state: HomeState,
    navigateToTransactionInfo: (Transaction) -> Unit
) {

    if (state.recentTransactions.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxWidth()
        ) {

            Column {

                Icon(
                    painter = painterResource(R.drawable.baseline_currency_exchange_24),
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "No recent transactions",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    } else {

        LazyColumn {

            items(state.recentTransactions) {
                TransactionCard(
                    transaction = it,
                    onClick = navigateToTransactionInfo
                )

                Spacer(Modifier.height(10.dp))
            }
        }
    }
}