package com.jk.cashcontrol.data.local_pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jk.cashcontrol.domain.local_pref.UserPref
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPrefImpl(
    private val datastore: DataStore<Preferences>
): UserPref {

    companion object {
        // To check if the app lock is enabled or disabled
        private val APP_LOCK_STATUS_KEY = booleanPreferencesKey("AppLockStatus")

        // to manage the app lock pin
        private val APP_LOCK_PIN_KEY = stringPreferencesKey("AppLockPin")

        // to check if the user is on the set up page or login page
        private val APP_LOCK_SETUP_KEY = booleanPreferencesKey("AppLockSetup")
    }

    override suspend fun enableAppLock() {
        datastore.edit { preferences ->
            preferences[APP_LOCK_STATUS_KEY] = true
        }
    }

    override suspend fun disableAppLock() {
        datastore.edit { preferences ->
            preferences[APP_LOCK_STATUS_KEY] = false
        }
    }

    override fun getAppLockStatus(): Flow<Boolean> {
        return datastore.data
            .catch { emptyPreferences() }
            .map { preferences ->
                preferences[APP_LOCK_STATUS_KEY] ?: false
            }
    }

    override suspend fun saveAppLockPin(pin: String) {
        datastore.edit { preferences ->
            preferences[APP_LOCK_PIN_KEY] = pin
        }
    }

    override fun getAppLockPin(): Flow<String> {
        return datastore.data
            .catch { emptyPreferences() }
            .map { preferences ->
                preferences[APP_LOCK_PIN_KEY] ?: ""
            }
    }
}