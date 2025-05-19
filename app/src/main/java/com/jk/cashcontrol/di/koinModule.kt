package com.jk.cashcontrol.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jk.cashcontrol.data.repository.TransactionRepositoryImpl
import com.jk.cashcontrol.domain.repository.TransactionRepository
import com.jk.cashcontrol.presentation.add_transaction.AddTransactionViewModel
import com.jk.cashcontrol.presentation.history.HistoryViewModel
import com.jk.cashcontrol.presentation.home.HomeViewModel
import com.jk.cashcontrol.presentation.login.LoginViewModel
import com.jk.cashcontrol.presentation.statistics.StatisticsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val koinModule = module {

    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    singleOf(::TransactionRepositoryImpl).bind<TransactionRepository>()

    viewModelOf(::HomeViewModel)
    viewModelOf(::AddTransactionViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::StatisticsViewModel)
}