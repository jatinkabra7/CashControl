package com.jk.cashcontrol.features.expense_tracker.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jk.cashcontrol.features.auth.domain.local_pref.UserPref
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPrefs: UserPref
): ViewModel() {

    val appLockStatus = userPrefs.getAppLockStatus()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    private fun enableAppLock() {
        viewModelScope.launch {
            userPrefs.enableAppLock()
        }
    }

    fun disableAppLock() {
        viewModelScope.launch {
            userPrefs.disableAppLock()
        }
    }

    fun savePin(pin: String) {
        viewModelScope.launch {
            userPrefs.saveAppLockPin(pin)
        }
    }

    fun onAction(action: SettingsActions) {
        when(action) {
            SettingsActions.ToggleAppLockStatus -> {
                if(appLockStatus.value == false) {
                    enableAppLock()
                } else {
                    disableAppLock()
                    savePin("")
                }
            }
        }
    }
}