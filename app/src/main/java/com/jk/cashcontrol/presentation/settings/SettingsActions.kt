package com.jk.cashcontrol.presentation.settings

sealed interface SettingsActions {
    data object ToggleAppLockStatus: SettingsActions
}