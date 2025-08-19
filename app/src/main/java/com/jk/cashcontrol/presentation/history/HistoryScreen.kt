package com.jk.cashcontrol.presentation.history

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.google.firebase.auth.FirebaseUser
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.presentation.home.components.TransactionCard
import com.jk.cashcontrol.presentation.theme.BackgroundColor
import com.jk.cashcontrol.presentation.theme.ButtonColor
import com.jk.cashcontrol.presentation.theme.ForegroundColor
import com.jk.cashcontrol.presentation.transaction.TransactionInfoCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    state: HistoryState,
    user: FirebaseUser?,
    onAction: (HistoryAction) -> Unit,
    isTransactionInfoSheetOpen: Boolean,
    toggleSheet: () -> Unit,
    isDeletionInProgress: Boolean,
    onDeleteTransaction: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(user?.uid.toString()) {
        onAction(HistoryAction.ReloadData)
    }

    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }

    val bottomSheetState = rememberModalBottomSheetState()

    if(isTransactionInfoSheetOpen && selectedTransaction != null) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = toggleSheet,
            scrimColor = ForegroundColor.copy(0.5f),
            containerColor = BackgroundColor,
            dragHandle = {}
        ) {
            TransactionInfoCard(
                transaction = selectedTransaction!!,
                onDeleteTransaction = {
                    onDeleteTransaction(it)
                },
                isLoading = isDeletionInProgress
            )
        }
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
                transaction.timestamp.lowercase().replace(",", "").contains(query) ||
                transaction.category.lowercase().contains(query)
    }

    val listState = rememberLazyListState()

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.history_screen_padding_medium))
    ) {

        Text(
            text = "History",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Light,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Start)
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.history_screen_spacer_medium)))

        SearchBar(
            query = searchQuery,
            onValueChange = { searchQuery = it }
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.history_screen_spacer_medium)))

        if (filteredTransactions.isEmpty()) {
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
                    style = MaterialTheme.typography.labelSmall,
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

                items(filteredTransactions, key = { it.timestampMillis }) {
                    Column(
                        modifier = Modifier
                            .animateItem()
                            .pointerInput(Unit) {
                                focusManager.clearFocus()
                            }
                    ) {

                        TransactionCard(
                            transaction = it,
                            onClick = {
                                selectedTransaction = Transaction(
                                    name = it.name,
                                    type = it.type,
                                    amount = it.amount,
                                    category = it.category,
                                    timestamp = it.timestamp,
                                    timestampMillis = it.timestampMillis
                                ).also {
                                    toggleSheet()
                                }
                            }
                        )

                        Spacer(Modifier.height(dimensionResource(id = R.dimen.history_screen_spacer_medium)))
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    TextField(
        value = query,
        onValueChange = { onValueChange(it) },
        placeholder = {
            Text(
                text = "Search by name, amount, date or category",
                modifier = Modifier.basicMarquee()
            )
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(100)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = ButtonColor,
            unfocusedContainerColor = ForegroundColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
