package com.jk.cashcontrol.presentation.transaction

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.domain.model.Transaction
import com.jk.cashcontrol.domain.model.TransactionType
import com.jk.cashcontrol.presentation.theme.BackgroundColor
import com.jk.cashcontrol.presentation.theme.CustomDarkOrange
import com.jk.cashcontrol.presentation.theme.CustomGreen
import com.jk.cashcontrol.presentation.theme.ForegroundColor

@Composable
fun TransactionInfoCard(
    transaction: Transaction,
    onDeleteTransaction: (Transaction) -> Unit,
    onEditTransactionName: (Transaction, newName: String) -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {

    val transactionColor =
        if (transaction.type == TransactionType.INCOME) CustomGreen
        else CustomDarkOrange

    val plusOrMinus =
        if (transaction.type == TransactionType.INCOME) '+'
        else '-'

    var isDeleteTransactionDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isEditTransactionNameDialogVisible by rememberSaveable { mutableStateOf(false) }
    var tName by remember { mutableStateOf(transaction.name) }

    if (isDeleteTransactionDialogVisible) {
        AlertDialog(
            onDismissRequest = { isDeleteTransactionDialogVisible = false },
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
                        text = if (!isLoading) "Delete" else "Deleting",
                        color = if (!isLoading) Color.White else Color.DarkGray
                    )
                }
            },
            dismissButton = {
                if (!isLoading) {
                    TextButton(
                        onClick = { isDeleteTransactionDialogVisible = false }
                    ) {
                        Text(
                            text = "Cancel",
                            color = Color.White
                        )
                    }
                }
            },
            containerColor = ForegroundColor
        )
    } else if (isEditTransactionNameDialogVisible) {
        AlertDialog(
            onDismissRequest = { isEditTransactionNameDialogVisible = false },
            title = {
                Text(
                    text = "Edit Transaction Name",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                OutlinedTextField(
                    value = tName,
                    onValueChange = { tName = it },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    enabled = tName.length >= 3,
                    onClick = {
                        onEditTransactionName(transaction, tName)
                        isEditTransactionNameDialogVisible = false
                    }
                ) {
                    Text(
                        text = "Save",
                        color = if (tName.length >= 3) Color.White else Color.DarkGray
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isEditTransactionNameDialogVisible = false
                    }
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

    Column(
        modifier = modifier.padding(20.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            Column {
                Text(
                    text = plusOrMinus + "${transaction.amount}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = transaction.timestamp,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }

            SuggestionChip(
                onClick = {},
                label = {
                    Text(
                        text = transaction.type.name,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Normal
                    )
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = transactionColor.copy(0.2f),
                    labelColor = transactionColor
                ),
                border = null
            )
        }

        Spacer(Modifier.height(10.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = ForegroundColor
            )
        ) {
            CardItem(
                title = "Name",
                content = transaction.name,
                actionButton = {
                    IconButton(onClick = { isEditTransactionNameDialogVisible = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Name",
                            tint = Color.White
                        )
                    }
                }
            )

            HorizontalDivider()

            CardItem(
                title = "Category",
                content = transaction.category
            )
        }

        Spacer(Modifier.height(10.dp))

        DeleteTransactionSection(
            onSwipe = {
                isDeleteTransactionDialogVisible = true
            }
        )

        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun CardItem(
    title: String,
    content: String,
    modifier: Modifier = Modifier,
    actionButton: @Composable () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Column {

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    overflow = Ellipsis
                )
            }

            actionButton()
        }


        Spacer(Modifier.height(5.dp))
    }
}

@Composable
private fun DeleteTransactionSection(
    onSwipe: () -> Unit,
    modifier: Modifier = Modifier
) {

    val transition = rememberInfiniteTransition(label = "gradientAnimation")

    val animatedOffset by transition.animateFloat(
        initialValue = -400f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradientAnimation"
    )

    val gradient = Brush.linearGradient(
        colors = listOf(Color.Black, Color.White, Color.Black),
        start = Offset(animatedOffset - 400f, 0f),
        end = Offset(animatedOffset + 400f, 0f)
    )

    var swipingOffsetX by remember { mutableFloatStateOf(0f) }

    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(100))
            .background(ForegroundColor)
    ) {

        this

        val width = LocalWindowInfo.current.containerSize.width.toFloat()

        Text(
            text = "Slide to Delete",
            style = TextStyle(
                brush = gradient
            ),
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.align(Alignment.Center)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            IconButton(
                onClick = {},
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = BackgroundColor,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .offset(x = swipingOffsetX.dp, y = 0.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                val newOffset = (swipingOffsetX + dragAmount.x / 3)
                                    .coerceIn(0f, width - 500f)

                                swipingOffsetX = newOffset
                            },

                            onDragEnd = {
                                if (swipingOffsetX >= 0.2 * width) {
                                    onSwipe()
                                }
                                swipingOffsetX = 0f
                            }
                        )
                    }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_keyboard_double_arrow_right_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}