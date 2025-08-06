package com.jk.cashcontrol.presentation.app_lock

import com.jk.cashcontrol.presentation.app_lock.components.AppLockStage

sealed interface AppLockActions {
    data class SavePin(val pin: String): AppLockActions
    data object EnableAppLock: AppLockActions
}