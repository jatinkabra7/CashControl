package com.jk.cashcontrol.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jk.cashcontrol.features.auth.data.local_pref.DataStoreFactory
import com.jk.cashcontrol.features.auth.data.local_pref.UserPrefImpl
import com.jk.cashcontrol.features.auth.domain.local_pref.UserPref
import com.jk.cashcontrol.features.auth.presentation.app_lock.AppLockViewModel
import com.jk.cashcontrol.features.auth.presentation.login.LoginViewModel
import com.jk.cashcontrol.features.expense_tracker.data.repository.TransactionRepositoryImpl
import com.jk.cashcontrol.features.expense_tracker.domain.repository.TransactionRepository
import com.jk.cashcontrol.features.expense_tracker.presentation.add_transaction.AddTransactionViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.history.HistoryViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.home.HomeViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.settings.SettingsViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.statistics.StatisticsViewModel
import com.jk.cashcontrol.features.expense_tracker.presentation.transaction.TransactionInfoViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinModule = module {

    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { DataStoreFactory.create(get()) }

    singleOf(::TransactionRepositoryImpl).bind<TransactionRepository>()
    singleOf(::UserPrefImpl).bind<UserPref>()

    viewModelOf(::HomeViewModel)
    viewModelOf(::AddTransactionViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::StatisticsViewModel)
    viewModelOf(::TransactionInfoViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::AppLockViewModel)
}