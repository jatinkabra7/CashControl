package com.jk.cashcontrol.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser
import com.jk.cashcontrol.presentation.home.components.BudgetCard
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.domain.model.TransactionType
import com.jk.cashcontrol.presentation.home.components.TransactionCard
import com.jk.cashcontrol.presentation.theme.CustomLightRed
import com.jk.cashcontrol.presentation.theme.CustomPink
import com.jk.cashcontrol.presentation.theme.CustomPurple

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    user : FirebaseUser?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(10.dp)
    ) {
        
        HeaderSection(state = state, username = user?.displayName.toString())

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Recent Transactions",
            style = TextStyle(
                brush = Brush.linearGradient(colors = listOf(CustomLightRed, CustomPink,
                    CustomPurple
                ))
            ),
            fontSize = 24.sp,
            color = Color.White
        )

        Spacer(Modifier.height(20.dp))

        HorizontalDivider()

        Spacer(Modifier.height(10.dp))

        RecentTransactions(state = state)


    }
}

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
    state: HomeState,
    username : String = "Username"
) {
    Column {
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
                text = if(username.length <= 16)username.take(16) else username.take(16)+"...",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge
            )
        }


        Spacer(Modifier.height(10.dp))

        BudgetCard(
            expense = state.expense,
            budget = state.budget
        )
    }
}

@Composable
fun RecentTransactions(
    modifier: Modifier = Modifier,
    state: HomeState
) {

    if(state.recentTransactions.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
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
    }
    else {

        LazyColumn {

            items(state.recentTransactions) {
                TransactionCard(
                    transaction = it
                )

                Spacer(Modifier.height(10.dp))

                HorizontalDivider()

                Spacer(Modifier.height(10.dp))
            }
        }
    }
}