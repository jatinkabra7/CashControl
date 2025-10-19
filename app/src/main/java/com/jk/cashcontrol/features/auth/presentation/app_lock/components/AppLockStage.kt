package com.jk.cashcontrol.features.auth.presentation.app_lock.components

enum class AppLockStage {
    SETUP, CONFIRM, LOGIN
}

/*

    SETUP -> for setting the login pin
    CONFIRM -> re entering the pin while setting up to confirm
    ERROR -> when the pin does not match
    LOGIN -> shown on app launch

 */