package com.jk.cashcontrol.presentation.home

sealed interface HomeEvents {
    data class ShowToast(val message: String) : HomeEvents
}