package com.jk.cashcontrol.features.auth.domain.local_pref

import kotlinx.coroutines.flow.Flow

interface UserPref {

    suspend fun enableAppLock()

    suspend fun disableAppLock()

    // true -> app lock enabled
    // false -> app lock disabled
    fun getAppLockStatus(): Flow<Boolean>

    suspend fun saveAppLockPin(pin: String)

    fun getAppLockPin(): Flow<String>
}