package com.jk.cashcontrol.presentation.add_transaction

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.Category
import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.domain.model.TransactionType
import com.jk.cashcontrol.presentation.add_transaction.components.AddTransactionTopBar
import com.jk.cashcontrol.presentation.theme.CustomDarkOrange
import com.jk.cashcontrol.presentation.theme.CustomGreen
import com.jk.cashcontrol.presentation.theme.ForegroundColor
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun AddTransactionScreen(
    state: AddTransactionState,
    onAction: (AddTransactionAction) -> Unit,
    navigateToHome: () -> Unit,
    onEvent: (AddTransactionEvent) -> Unit,
    event: Flow<AddTransactionEvent>,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    LaunchedEffect(event) {
        event.collect {
            when (it) {
                is AddTransactionEvent.ShowToast -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val transactionColor =
        if (state.transactionType == TransactionType.INCOME) CustomGreen else CustomDarkOrange

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
            .verticalScroll(rememberScrollState())
    )
    {

        AddTransactionTopBar(
            state = state,
            navigateToHome = navigateToHome
        )

        AmountSection(
            amount = state.amountTextFieldValue,
            onAction = { onAction(it) }
        )

        NameSection(
            transactionName = state.nameTextFieldValue,
            onAction = { onAction(it) }
        )

        CategorySection(
            transactionType = state.transactionType,
            transactionCategory = state.category,
            onAction = { onAction(it) }
        )

        DatePickerSection(
            isDatePickerDialogOpen = state.isDatePickerDialogOpen,
            timestamp = state.timestamp,
            onAction = { onAction(it) }
        )

        Spacer(Modifier.weight(1f))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = transactionColor.copy(0.2f),
                contentColor = transactionColor
            ),
            onClick = {
                if (state.amountTextFieldValue.isBlank() || state.amountTextFieldValue.toFloatOrNull() == 0f) {
                    onEvent(AddTransactionEvent.ShowToast("Amount cannot be 0."))
                } else if (state.nameTextFieldValue.length < 3) {
                    onEvent(AddTransactionEvent.ShowToast("Name cannot have less than 3 characters."))
                } else {

                    onAction(
                        AddTransactionAction.OnSubmit(
                            Transaction(
                                timestamp = state.timestamp,
                                timestampMillis = System.currentTimeMillis(),
                                category = state.category,
                                name = state.nameTextFieldValue,
                                type = state.transactionType!!,
                                amount = state.amountTextFieldValue.toFloat()
                            )
                        )
                    )
                    navigateToHome()
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(shape = RoundedCornerShape(dimensionResource(R.dimen.add_transaction_corner_radius)))

        ) {
            Text(
                text = "SUBMIT",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(Modifier.height(dimensionResource(R.dimen.add_transaction_spacing_medium)))

    }
}

@Composable
private fun AmountSection(
    amount: String,
    onAction: (AddTransactionAction) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier.padding(dimensionResource(id = R.dimen.add_transaction_padding_medium))) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.add_transaction_corner_radius)))
                .align(Alignment.CenterHorizontally)
                .background(ForegroundColor)

        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = amount,
                placeholder = {
                    Text(
                        text = "Enter Amount",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                },
                onValueChange = { onAction(AddTransactionAction.OnAmountTextFieldValueChange(it)) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color.White
                ),
                textStyle = MaterialTheme.typography.titleMedium,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )
        }
    }
}

@Composable
private fun NameSection(
    transactionName: String,
    onAction: (AddTransactionAction) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier.padding(dimensionResource(id = R.dimen.add_transaction_padding_medium))) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.add_transaction_corner_radius)))
                .align(Alignment.CenterHorizontally)
                .background(ForegroundColor)

        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = if (transactionName == "New Transaction") "" else transactionName,
                placeholder = {
                    Text(
                        text = "Transaction Name",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                },
                onValueChange = { onAction(AddTransactionAction.OnNameTextFieldValueChange(it)) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color.White
                ),
                textStyle = MaterialTheme.typography.titleMedium
            )

        }
    }
}


@Composable
private fun CategorySection(
    transactionType: TransactionType?,
    transactionCategory: String,
    onAction: (AddTransactionAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val incomeCategories = listOf(
        Category(name = "Salary", icon = R.drawable.salary),
        Category(name = "Business", icon = R.drawable.business),
        Category(name = "Investment", icon = R.drawable.investment),
        Category(name = "Gifts", icon = R.drawable.gifts),
        Category(name = "Other", icon = R.drawable.other),
    )

    val expenseCategories = listOf(
        Category(name = "Food", icon = R.drawable.food),
        Category(name = "Business", icon = R.drawable.business),
        Category(name = "Movies", icon = R.drawable.entertainment),
        Category(name = "Transport", icon = R.drawable.transport),
        Category(name = "Shopping", icon = R.drawable.shopping),
        Category(name = "Bills", icon = R.drawable.bills),
        Category(name = "Education", icon = R.drawable.education),
        Category(name = "Other", icon = R.drawable.other),
    )

    val categories =
        if (transactionType == TransactionType.INCOME) incomeCategories
        else expenseCategories

    Column(modifier.padding(dimensionResource(id = R.dimen.add_transaction_padding_medium))) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.add_transaction_corner_radius)))
                .fillMaxWidth()
                .background(ForegroundColor)
                .padding(vertical = dimensionResource(id = R.dimen.add_transaction_padding_medium))

        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Choose Category",
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(id = R.dimen.add_transaction_padding_medium),
                            bottom = dimensionResource(id = R.dimen.add_transaction_padding_large)
                        )
                        .align(Alignment.Start)
                )

                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.add_transaction_padding_large)),
                    horizontalArrangement = Arrangement.Center,
                    maxItemsInEachRow = 3,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    categories.forEach { category ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (transactionCategory == category.name) Color.White.copy(
                                    0.1f
                                ) else Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (transactionCategory == category.name) Color.White else Color.Transparent,
                            ),
                            modifier = Modifier
                                .width(100.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = null
                                ) {
                                    onAction(AddTransactionAction.OnCategorySelection(category = category.name))
                                }
                        ) {

                            Image(
                                painter = painterResource(category.icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(30.dp)
                                    .align(Alignment.CenterHorizontally)
                            )

                            Text(
                                text = category.name,
                                color = Color.White,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .align(Alignment.CenterHorizontally)
                            )

                        }
                    }

                }

            }

        }
    }
}

fun LocalDateTime.toMillis() =
    this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerSection(
    isDatePickerDialogOpen: Boolean,
    timestamp: String,
    onAction: (AddTransactionAction) -> Unit,
    modifier: Modifier = Modifier
) {

    val dateTime = LocalDateTime.now()

    val datePickerState = rememberDatePickerState(
        yearRange = 2000..LocalDateTime.now().year,
        initialSelectedDateMillis = dateTime.toMillis(),
        initialDisplayMode = DisplayMode.Picker,
        initialDisplayedMonthMillis = null
    )

    if (isDatePickerDialogOpen) {

        DatePickerDialog(
            onDismissRequest = { onAction(AddTransactionAction.OnDatePickerDismiss) },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (datePickerState.selectedDateMillis != null && datePickerState.selectedDateMillis!! <= dateTime.toMillis()) {
                            onAction(AddTransactionAction.OnPickDateConfirmButton(datePickerState.selectedDateMillis.toString()))
                        } else {
                            onAction(AddTransactionAction.OnDatePickerDismiss)
                        }
                    }
                ) {
                    Text(text = "Done", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onAction(AddTransactionAction.OnDatePickerDismiss) }
                ) {
                    Text(text = "Cancel", color = Color.White)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = ForegroundColor
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = ForegroundColor,
                    currentYearContentColor = Color.White,
                    todayDateBorderColor = Color.White,
                    dayContentColor = Color.White,
                    dividerColor = Color.White,
                    yearContentColor = Color.White,
                    todayContentColor = Color.White,
                    titleContentColor = Color.White,
                    subheadContentColor = Color.White,
                    weekdayContentColor = Color.White,
                    headlineContentColor = Color.White,
                    navigationContentColor = Color.White,
                    disabledYearContentColor = Color.White,
                    selectedYearContentColor = Color.White,
                    disabledDayContentColor = Color.Transparent,
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = Color.White.copy(0.3f),
                    selectedYearContainerColor = Color.White.copy(0.3f)
                )
            )
        }
    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.add_transaction_padding_medium))
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.add_transaction_corner_radius)))
            .background(ForegroundColor)
            .padding(dimensionResource(id = R.dimen.add_transaction_padding_medium))

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Pick a date",
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = R.dimen.add_transaction_padding_medium))
                    .align(Alignment.Start)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = timestamp,
                    fontWeight = FontWeight.Normal,
                    fontSize = 22.sp,
                    color = Color.White.copy(0.7f),
                    modifier = Modifier
                )

                IconButton(
                    onClick = { onAction(AddTransactionAction.OnDateEditButtonClick) },
                    modifier = Modifier
                ) {
                    Icon(
                        painter = painterResource(R.drawable.edit),
                        contentDescription = null,
                        tint = Color.White.copy(0.8f),
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        }
    }
}