package com.kassaev.planner.di

import android.app.Application
import com.kassaev.planner.data.AppDatabase
import com.kassaev.planner.data.di.dataModule
import com.kassaev.planner.domain.di.domainModule
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    appModule,
                    domainModule,
                    dataModule
                )
            )
        }
        database = getKoin().get<AppDatabase>()
    }

    override fun onTerminate() {
        super.onTerminate()
        database.close()
    }
}