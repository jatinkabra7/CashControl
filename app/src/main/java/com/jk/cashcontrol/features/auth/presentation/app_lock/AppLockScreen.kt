package com.jk.cashcontrol.features.auth.presentation.app_lock

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.jk.cashcontrol.R
import com.jk.cashcontrol.features.auth.presentation.app_lock.components.AppLockStage
import com.jk.cashcontrol.features.auth.presentation.app_lock.components.InputIndicatorItem
import com.jk.cashcontrol.features.auth.presentation.app_lock.components.NumericButton
import kotlinx.coroutines.delay

@Composable
fun AppLockScreen(
    correctPin: String,
    appLockStatus: Boolean,
    onAction: (AppLockActions) -> Unit,
    navigateToHome: () -> Unit,
    navigateUp: () -> Unit,
    onFingerprintClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var pin by rememberSaveable { mutableStateOf("") }
    var setupPin by rememberSaveable { mutableStateOf("") }
    var stage by rememberSaveable { mutableStateOf(AppLockStage.SETUP) }
    if (appLockStatus) stage = AppLockStage.LOGIN
    var message by rememberSaveable { mutableStateOf(getStageMessage(stage)) }

    LaunchedEffect(pin.length) {
        if (pin.length == 4) {
            when (stage) {
                AppLockStage.SETUP -> {
                    setupPin = pin
                    pin = ""
                    stage = AppLockStage.CONFIRM
                    message = getStageMessage(stage)
                }

                AppLockStage.CONFIRM -> {
                    if (pin == setupPin) {
                        delay(1500)
                        onAction(AppLockActions.SavePin(pin))
                        onAction(AppLockActions.EnableAppLock)
                        navigateUp()
                    } else {
                        pin = ""
                        Toast.makeText(context, "Incorrect pin, try again", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                AppLockStage.LOGIN -> {
                    if (pin == correctPin) {
                        delay(1500)
                        navigateToHome()
                    } else {
                        pin = ""
                        Toast.makeText(context, "Incorrect pin, try again", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(horizontal = dimensionResource(id = R.dimen.app_lock_horizontal_padding))
    ) {
        Spacer(Modifier.weight(1f))

        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.app_lock_message_spacer)))

        InputIndicator(
            pin = pin,
            correctPin = correctPin,
            setupPin = setupPin
        )

        Spacer(Modifier.weight(1f))

        NumericButtonSection(
            onButtonClick = { digit -> if (pin.length < 4) pin += "$digit" },
            onBackspaceClick = { if (pin.length < 4) pin = pin.dropLast(1) },
            onFingerprintClick = onFingerprintClick,
            fingerPrintButtonColor = if (stage == AppLockStage.LOGIN) Color.White else Color.Black
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.app_lock_bottom_spacer)))
    }
}

@Composable
private fun NumericButtonSection(
    onButtonClick: (Int) -> Unit,
    onBackspaceClick: () -> Unit,
    onFingerprintClick: () -> Unit,
    fingerPrintButtonColor: Color,
    modifier: Modifier = Modifier
) {
    val buttonList = listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9))

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.app_lock_numeric_row_spacing)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        buttonList.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { digit ->
                    NumericButton(
                        digit = digit,
                        buttonSize = dimensionResource(id = R.dimen.app_lock_numeric_button_size),
                        onClick = { onButtonClick(digit) }
                    )
                }
            }
        }

        // last row with fingerprint, 0 and backspace
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onFingerprintClick) {
                Icon(
                    painter = painterResource(R.drawable.outline_fingerprint_24),
                    contentDescription = "Use Biometric",
                    tint = fingerPrintButtonColor,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.app_lock_icon_size))
                )
            }

            NumericButton(
                digit = 0,
                buttonSize = dimensionResource(id = R.dimen.app_lock_numeric_button_size),
                onClick = { onButtonClick(0) }
            )

            IconButton(onClick = onBackspaceClick) {
                Icon(
                    painter = painterResource(R.drawable.outline_backspace_24),
                    contentDescription = "Backspace",
                    tint = Color.White,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.app_lock_icon_size))
                )
            }
        }
    }
}

@Composable
private fun InputIndicator(
    pin: String,
    correctPin: String,
    setupPin: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.app_lock_input_height))
            .fillMaxWidth()
    ) {
        for (i in 1..4) {
            InputIndicatorItem(
                pin = pin,
                correctPin = correctPin,
                setupPin = setupPin,
                position = i,
                isActive = pin.length >= i
            )
            if (i < 4) Spacer(Modifier.width(dimensionResource(id = R.dimen.app_lock_input_spacing)))
        }
    }
}


private fun getStageMessage(stage: AppLockStage): String {
    return when (stage) {
        AppLockStage.SETUP -> "Setup 4-Digit Login Pin"
        AppLockStage.CONFIRM -> "Re-Enter Pin to Confirm"
        AppLockStage.LOGIN -> "Enter 4-Digit Login Pin"
    }
}