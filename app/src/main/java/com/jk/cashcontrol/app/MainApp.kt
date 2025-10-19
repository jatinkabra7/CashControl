package com.jk.cashcontrol.app

import android.app.Application
import com.jk.cashcontrol.core.di.koinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApp)
            modules(koinModule)
        }
    }
}