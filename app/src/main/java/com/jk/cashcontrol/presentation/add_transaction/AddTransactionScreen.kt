package com.jk.cashcontrol.presentation.add_transaction

import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.ScrollableTabRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.Category
import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.domain.model.TransactionType
import com.jk.cashcontrol.presentation.add_transaction.components.AddTransactionTopBar
import com.jk.cashcontrol.presentation.theme.CustomDarkBlue
import com.jk.cashcontrol.presentation.theme.CustomDarkGreen
import com.jk.cashcontrol.presentation.theme.CustomDarkOrange
import com.jk.cashcontrol.presentation.theme.CustomLightBlue
import com.jk.cashcontrol.presentation.theme.CustomLightGreen
import com.jk.cashcontrol.presentation.theme.CustomLightOrange
import com.jk.cashcontrol.presentation.theme.CustomLightRed
import com.jk.cashcontrol.presentation.theme.CustomPink
import com.jk.cashcontrol.presentation.theme.CustomPurple
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.ZoneId


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddTransactionScreen(
    modifier: Modifier = Modifier,
    state: AddTransactionState,
    onAction: (AddTransactionAction) -> Unit,
    navigateToHome : () -> Unit,
    onEvent : (AddTransactionEvent) -> Unit,
    event : Flow<AddTransactionEvent>
) {

    val context = LocalContext.current

    LaunchedEffect(event) {
        event.collect {
            Toast.makeText(context,"Amount cannot be 0", Toast.LENGTH_SHORT).show()
        }

    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .windowInsetsPadding(WindowInsets.systemBars)
    )
    {
        AddTransactionTopBar(
            state = state,
            navigateToHome = { navigateToHome() }
        )

        Spacer(Modifier.height(20.dp))

        AmountSection(
            state = state,
            onAction = {onAction(it)}
        )

        Spacer(Modifier.height(20.dp))

        NameSection(
            state = state,
            onAction = {onAction(it)}
        )

        Spacer(Modifier.height(20.dp))

        CategorySection(
            state = state,
            onAction = {onAction(it)}
        )

        Spacer(Modifier.height(20.dp))

        DatePickerSection(
            state = state,
            onAction = {onAction(it)}
        )

        Spacer(Modifier.weight(1f))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            onClick = {
                if(state.amountTextFieldValue.isBlank() || state.amountTextFieldValue.toFloatOrNull() == 0f) {
                    onEvent(AddTransactionEvent.ShowToast)
                }
                else {

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
                .width(200.dp)
                .background(
                    shape = RoundedCornerShape(20.dp),
                    brush = Brush.linearGradient(listOf(CustomLightBlue, CustomDarkBlue))
                )

        ) {
            Text(
                text = "Submit",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(10.dp))

    }
}

@Composable
private fun AmountSection(
    modifier: Modifier = Modifier,
    state: AddTransactionState,
    onAction : (AddTransactionAction) -> Unit
) {

    val brush =
        if(state.transactionType == TransactionType.INCOME) {
            Brush.verticalGradient(listOf(CustomLightGreen, CustomDarkGreen))
        }
        else {
            Brush.verticalGradient(listOf(CustomLightOrange, CustomDarkOrange))
        }

    Column(modifier) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .background(
                    brush
                )

        ) {

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.amountTextFieldValue,
                onValueChange = {onAction(AddTransactionAction.OnAmountTextFieldValueChange(it))},
                singleLine = true,
                label = {
                    Text(
                        text = "Enter Amount",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    cursorColor = Color.DarkGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(fontSize = 30.sp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )
        }
    }
}

@Composable
private fun NameSection(
    modifier: Modifier = Modifier,
    state: AddTransactionState,
    onAction : (AddTransactionAction) -> Unit
) {

    val brush = Brush.verticalGradient(listOf(Color.Red.copy(0.7f),Color.Red))

    Column(modifier) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .background(
                    brush
                )

        ) {

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = if(state.nameTextFieldValue == "New Transaction") "" else state.nameTextFieldValue,
                onValueChange = {onAction(AddTransactionAction.OnNameTextFieldValueChange(it))},
                singleLine = true,
                label = {
                    Text(
                        text = "Transaction Name",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    cursorColor = Color.Black,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(fontSize = 20.sp)
            )
        }
    }
}

@Composable
private fun CategorySection(
    modifier: Modifier = Modifier,
    state : AddTransactionState,
    onAction: (AddTransactionAction) -> Unit
) {
    val incomeCategories = listOf<Category>(
        Category(name = "Salary", icon = R.drawable.salary),
        Category(name = "Business", icon = R.drawable.business),
        Category(name = "Investment", icon = R.drawable.investment),
        Category(name = "Gifts", icon = R.drawable.gifts),
        Category(name = "Other", icon = R.drawable.other),
    )

    val expenseCategories = listOf<Category>(
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
        if(state.transactionType == TransactionType.INCOME) incomeCategories
        else expenseCategories

    Column(modifier) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(CustomLightRed, CustomPink, CustomPurple)
                    )
                )
                .padding(vertical = 10.dp)

        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Choose Category",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 10.dp, bottom = 10.dp)
                        .align(Alignment.Start)
                )

                ScrollableTabRow(
                    containerColor = Color.Transparent,
                    selectedTabIndex = categories.size-1,
                    edgePadding = 0.dp,
                    indicator = {},
                    modifier = Modifier
                        .background(Color.Transparent)
                ) {

                    categories.forEach {category ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if(state.category == category.name) Color.Black.copy(0.3f) else Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .width(100.dp)
                                .clickable {
                                    onAction(AddTransactionAction.OnCategorySelection(category = category.name))
                                }
                        ) {
                            Image(
                                painter = painterResource(category.icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.CenterHorizontally)
                            )

                            Text(
                                text = category.name,
                                color = Color.White,
                                modifier = Modifier
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
    modifier: Modifier = Modifier,
    state: AddTransactionState,
    onAction: (AddTransactionAction) -> Unit
) {

    val dateTime = LocalDateTime.now()

    val datePickerState = rememberDatePickerState(
        yearRange = 2000..LocalDateTime.now().year,
        initialSelectedDateMillis = dateTime.toMillis(),
        initialDisplayMode = DisplayMode.Picker,
        initialDisplayedMonthMillis = null
    )

    if(state.isDatePickerDialogOpen) {

        DatePickerDialog(
            onDismissRequest = {onAction(AddTransactionAction.OnDatePickerDismiss)},
            confirmButton = {
                TextButton(
                    onClick = {
                        if(datePickerState.selectedDateMillis != null && datePickerState.selectedDateMillis!! <= dateTime.toMillis()) {
                            onAction(AddTransactionAction.OnPickDateConfirmButton(datePickerState.selectedDateMillis.toString()))
                        }
                        else {
                            onAction(AddTransactionAction.OnDatePickerDismiss)
                        }
                    }
                ) {
                    Text(text = "Done", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {onAction(AddTransactionAction.OnDatePickerDismiss)}
                ) {
                    Text(text = "Cancel", color = Color.White)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.DarkGray
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.DarkGray,
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
                    selectedDayContainerColor = Color.White.copy(0.5f),
                    selectedYearContainerColor = Color.White.copy(0.5f)
                )
            )
        }
    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .background(
                Color.DarkGray
            )
            .padding(10.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Pick a date",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .align(Alignment.Start)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = state.timestamp,
                    fontWeight = FontWeight.Normal,
                    fontSize = 22.sp,
                    color = Color.White.copy(0.7f),
                    modifier = Modifier
                )

                IconButton(
                    onClick = {onAction(AddTransactionAction.OnDateEditButtonClick)},
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