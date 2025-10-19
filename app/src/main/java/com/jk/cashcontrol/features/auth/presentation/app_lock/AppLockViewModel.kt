package com.jk.cashcontrol.features.auth.presentation.app_lock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.cashcontrol.features.auth.domain.local_pref.UserPref
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppLockViewModel(
    private val userPrefs: UserPref
): ViewModel() {

    val appLockStatus = userPrefs.getAppLockStatus()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    val appLockPin = userPrefs.getAppLockPin()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ""
        )

    private fun savePin(pin: String) {
        viewModelScope.launch {
            userPrefs.saveAppLockPin(pin)
        }
    }

    private fun enableAppLock() {
        viewModelScope.launch {
            userPrefs.enableAppLock()
        }
    }

    fun onAction(action: AppLockActions) {
        when(action) {
            is AppLockActions.SavePin -> {
                savePin(action.pin)
            }

            AppLockActions.EnableAppLock -> enableAppLock()
        }
    }
}