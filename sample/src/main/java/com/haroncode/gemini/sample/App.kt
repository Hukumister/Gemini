package com.haroncode.gemini.sample

import android.app.Application
import com.haroncode.gemini.sample.di.AppModule
import com.haroncode.gemini.sample.di.DI
import java.util.UUID
import timber.log.Timber
import timber.log.Timber.DebugTree
import toothpick.configuration.Configuration
import toothpick.ktp.KTP

class App : Application() {

    companion object {
        var appCode: String = ""
    }

    override fun onCreate() {
        super.onCreate()

        initToothpick()
        initTimber()
        initAppScope()
        appCode = UUID.randomUUID().toString()
    }

    private fun initToothpick() {
        if (BuildConfig.DEBUG) {
            KTP.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            KTP.setConfiguration(Configuration.forProduction())
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    private fun initAppScope() {
        val openScope = KTP.openScope(DI.APP_SCOPE)
        openScope.installModules(AppModule(this))
        openScope.inject(this)
    }
}
