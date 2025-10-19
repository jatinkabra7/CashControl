package com.jk.cashcontrol.features.expense_tracker.presentation.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser
import com.jk.cashcontrol.R
import com.jk.cashcontrol.app.presentation.theme.BackgroundColor
import com.jk.cashcontrol.app.presentation.theme.ForegroundColor
import com.jk.cashcontrol.features.expense_tracker.domain.model.Transaction
import com.jk.cashcontrol.features.expense_tracker.presentation.home.components.AddIncomeExpenseCard
import com.jk.cashcontrol.features.expense_tracker.presentation.home.components.BudgetCard
import com.jk.cashcontrol.features.expense_tracker.presentation.home.components.TransactionCard
import com.jk.cashcontrol.features.expense_tracker.presentation.transaction.TransactionInfoCard
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    user: FirebaseUser?,
    onAction: (HomeActions) -> Unit,
    onEvent: (HomeEvents) -> Unit,
    onIncomeButtonClick: () -> Unit,
    onExpenseButtonClick: () -> Unit,
    isTransactionInfoSheetOpen: Boolean,
    toggleSheet: () -> Unit,
    isDeletionInProgress: Boolean,
    onDeleteTransaction: (Transaction) -> Unit,
    onEditTransactionName: (Transaction, newName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(user?.uid.toString()) {
        onAction(HomeActions.ReloadData)
    }

    if (state.isEditBudgetDialogOpen) {

        AlertDialog(
            onDismissRequest = { onAction(HomeActions.OnEditBudgetDismiss) },
            title = {
                Text(
                    text = "Edit Budget",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                OutlinedTextField(
                    value = state.editBudgetTextFieldValue,
                    onValueChange = { onAction(HomeActions.OnEditBudgetTextFieldValueChange(it)) },
                    supportingText = {
                        Text(
                            text = "Budget will be updated to the new value"
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            },
            confirmButton = {
                TextButton(
                    enabled = state.editBudgetTextFieldValue.isNotEmpty(),
                    onClick = { onAction(HomeActions.OnEditBudgetConfirm(state.editBudgetTextFieldValue.toFloat())) }
                ) {
                    Text(
                        text = "Done",
                        color = if (state.editBudgetTextFieldValue.isNotEmpty()) Color.White else Color.DarkGray
                    )
                }
            },
            dismissButton = {

                TextButton(
                    onClick = { onAction(HomeActions.OnEditBudgetDismiss) }
                ) {
                    Text(
                        text = "Cancel",
                        color = Color.White
                    )
                }

            },
            containerColor = ForegroundColor
        )
    } else if (state.isNewBudgetDialogOpen) {
        AlertDialog(
            onDismissRequest = { onAction(HomeActions.OnNewBudgetDismiss) },
            title = {
                Text(
                    text = "New Budget",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                OutlinedTextField(
                    value = state.newBudgetTextFieldValue,
                    onValueChange = { onAction(HomeActions.OnNewBudgetTextFieldValueChange(it)) },
                    supportingText = {
                        Text(
                            text = "Expense will be reset"
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            },
            confirmButton = {
                TextButton(
                    enabled = state.newBudgetTextFieldValue.isNotEmpty(),
                    onClick = { onAction(HomeActions.OnNewBudgetConfirm(state.newBudgetTextFieldValue.toFloat())) }
                ) {
                    Text(
                        text = "Done",
                        color = if (state.newBudgetTextFieldValue.isNotEmpty()) Color.White else Color.DarkGray
                    )
                }
            },
            dismissButton = {

                TextButton(
                    onClick = { onAction(HomeActions.OnNewBudgetDismiss) }
                ) {
                    Text(
                        text = "Cancel",
                        color = Color.White
                    )
                }

            },
            containerColor = ForegroundColor
        )
    }

    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }

    val bottomSheetState = rememberModalBottomSheetState()

    if (isTransactionInfoSheetOpen && selectedTransaction != null) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = toggleSheet,
            scrimColor = ForegroundColor.copy(0.5f),
            containerColor = BackgroundColor,
            dragHandle = {}
        ) {
            TransactionInfoCard(
                transaction = selectedTransaction!!,
                onDeleteTransaction = { onDeleteTransaction(it) },
                onEditTransactionName = { transaction, newName ->
                    onEditTransactionName(transaction, newName)
                },
                isLoading = isDeletionInProgress
            )
        }
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(id = R.dimen.home_screen_horizontal_padding))
    ) {

        HeaderSection(
            state = state,
            onAction = { onAction(it) },
            onIncomeButtonClick = onIncomeButtonClick,
            onExpenseButtonClick = {
                if(state.budget == 0f) {
                    onEvent(HomeEvents.ShowToast("Please set a budget first"))
                } else onExpenseButtonClick()
            }
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.home_spacer_large)))

        Text(
            text = "Recent Transactions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Light,
            color = Color.White
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.home_spacer_small)))

        HorizontalDivider(Modifier.fillMaxWidth(0.7f))

        Spacer(Modifier.height(dimensionResource(id = R.dimen.home_spacer_medium)))

        RecentTransactions(
            recentTransactions = state.recentTransactions,
            onTransactionClick = {
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
    }
}

@Composable
fun HeaderSection(
    state: HomeState,
    onAction: (HomeActions) -> Unit,
    onIncomeButtonClick: () -> Unit,
    onExpenseButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        Text(
            text = "Home",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Light,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Start)
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.home_spacer_medium)))

        BudgetCard(
            expense = state.expense,
            budget = state.budget,
            remaining = state.remaining,
            onAction = { onAction(it) }
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.home_spacer_large)))

        AddIncomeExpenseCard(
            onIncomeButtonClick = onIncomeButtonClick,
            onExpenseButtonClick = onExpenseButtonClick
        )
    }
}

@Composable
fun RecentTransactions(
    recentTransactions: List<Transaction>,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {

    if (recentTransactions.isEmpty()) {
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

        recentTransactions.forEach {
            Column {
                TransactionCard(
                    transaction = it,
                    onClick = onTransactionClick
                )
                Spacer(Modifier.height(dimensionResource(id = R.dimen.home_spacer_medium)))
            }
        }
    }
}