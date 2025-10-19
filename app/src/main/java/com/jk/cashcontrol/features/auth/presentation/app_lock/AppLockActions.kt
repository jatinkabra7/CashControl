package com.jk.cashcontrol.features.auth.presentation.app_lock

sealed interface AppLockActions {
    data class SavePin(val pin: String): AppLockActions
    data object EnableAppLock: AppLockActions
}