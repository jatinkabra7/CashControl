package com.jk.cashcontrol

import android.app.Application
import androidx.compose.ui.platform.LocalContext
import com.jk.cashcontrol.di.koinModule
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