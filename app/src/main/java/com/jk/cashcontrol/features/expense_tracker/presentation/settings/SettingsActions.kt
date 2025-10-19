package com.jk.cashcontrol.features.expense_tracker.presentation.settings

sealed interface SettingsActions {
    data object ToggleAppLockStatus: SettingsActions
}