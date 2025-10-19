package com.jk.cashcontrol.features.auth.data.local_pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.jk.cashcontrol.features.auth.data.utils.Constants

object DataStoreFactory {

    fun create(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() })
        ) {
            context.preferencesDataStoreFile(name = Constants.CASH_CONTROL_LOCAL_PREFS)
        }
    }
}