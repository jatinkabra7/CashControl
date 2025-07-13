package com.jk.cashcontrol.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.presentation.home.components.TransactionCard

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    state: HistoryState,
    user : FirebaseUser?,
    onAction: (HistoryAction) -> Unit,
    navigateToTransactionInfo: (Transaction) -> Unit
) {

    LaunchedEffect(user?.uid.toString()) {
        onAction(HistoryAction.ReloadData)
    }

    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    val transactions = state.allTransactions

    val filteredTransactions = transactions.filter { transaction ->
        val query = searchQuery.lowercase()
        transaction.name.lowercase().contains(query) ||
        transaction.amount.toString().contains(query) ||
        transaction.timestamp.lowercase().contains(query) ||
        transaction.timestamp.lowercase().replace(",","").contains(query) ||
        transaction.category.lowercase().contains(query)
    }

    val listState = rememberLazyListState()

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(10.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        Text(
            text = "History",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Start)
                .pointerInput(Unit) {
                    focusManager.clearFocus()
                }
        )

        Spacer(Modifier.height(10.dp))

        SearchBar(
            query = searchQuery,
            onValueChange = {searchQuery = it}
        )

        Spacer(Modifier.height(30.dp))

        if(filteredTransactions.isEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .pointerInput(Unit) {
                        focusManager.clearFocus()
                    }
            ) {

                Icon(
                    painter = painterResource(R.drawable.baseline_currency_exchange_24),
                    contentDescription = null,
                    tint = Color.DarkGray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "No transactions",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .pointerInput(Unit) {
                        focusManager.clearFocus()
                    }
            ) {

                items(filteredTransactions, key = {it.timestampMillis}) {
                    Column(
                        modifier = Modifier.animateItem().pointerInput(Unit) {
                            focusManager.clearFocus()
                        }
                    ) {

                        TransactionCard(
                            transaction = it,
                            onClick = navigateToTransactionInfo
                        )

                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    }

}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query : String,
    onValueChange : (String) -> Unit
) {

    TextField(
        value = query,
        onValueChange = { onValueChange(it) },
        placeholder = { Text("Search by name, amount, date or category", modifier = Modifier.basicMarquee()) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100))
        ,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.DarkGray,
            unfocusedContainerColor = Color.DarkGray.copy(0.5f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )

}