package com.jk.cashcontrol.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jk.cashcontrol.R
import com.jk.cashcontrol.presentation.settings.components.SettingItem
import com.jk.cashcontrol.presentation.theme.BackgroundColor
import com.jk.cashcontrol.presentation.theme.CustomDarkOrange

@Composable
fun SettingsScreen(
    appLockStatus: Boolean,
    onAction: (SettingsActions) -> Unit,
    navigateToAppInfoScreen: () -> Unit,
    navigateToAppLockScreen: () -> Unit,
    onLogout: () -> Unit,
    onDeleteAccount: () -> Unit,
    isAccountDeleting: Boolean,
    modifier: Modifier = Modifier
) {

    var deleteAccountDialogTextFieldValue by rememberSaveable { mutableStateOf("") }

    var isDeleteAccountDialogVisible by rememberSaveable { mutableStateOf(false) }

    if(isDeleteAccountDialogVisible) {
        AlertDialog(
            onDismissRequest = {isDeleteAccountDialogVisible = false},
            title = {
                Text(
                    text = "DELETE ACCOUNT",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                OutlinedTextField(
                    enabled = !isAccountDeleting,
                    value = deleteAccountDialogTextFieldValue,
                    onValueChange = { deleteAccountDialogTextFieldValue = it },
                    supportingText = {
                        Text(
                            text = "Type \"DELETE\" and press Delete. Please select the current account when prompted.",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    enabled = deleteAccountDialogTextFieldValue == "DELETE" && !isAccountDeleting,
                    onClick = onDeleteAccount
                ) {
                    Text(
                        text = if(!isAccountDeleting) "Delete" else "Deleting"
                    )
                }
            },
            dismissButton = {
                if(!isAccountDeleting) {
                    TextButton(
                        onClick = { isDeleteAccountDialogVisible = false }
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
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
            .background(BackgroundColor)
            .padding(10.dp)
    ) {

        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Light,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Start)
        )

        Spacer(Modifier.height(20.dp))

        SettingItem(
            label = "App Info",
            contentDescription = "App Info",
            contentColor = Color.White,
            icon = R.drawable.outline_info_24,
            onClick = navigateToAppInfoScreen
        )

        Spacer(Modifier.height(10.dp))

        SettingItem(
            label = "App Lock",
            contentDescription = "App Lock",
            contentColor = Color.White,
            icon = R.drawable.outline_lock_24,
            showSwitch = true,
            isSwitchEnabled = appLockStatus,
            onClick = {
                if(appLockStatus == false) {
                    navigateToAppLockScreen()
                }
                else onAction(SettingsActions.ToggleAppLockStatus)
            }
        )

        Spacer(Modifier.height(10.dp))

        SettingItem(
            label = "Logout",
            contentDescription = "Logout",
            contentColor = CustomDarkOrange,
            icon = R.drawable.icons8_google_logo,
            onClick = onLogout
        )

        Spacer(Modifier.height(10.dp))

        SettingItem(
            label = "Delete Account",
            contentDescription = "Delete Account",
            contentColor = CustomDarkOrange,
            icon = R.drawable.baseline_dangerous_24,
            onClick = { isDeleteAccountDialogVisible = true }
        )
    }
}