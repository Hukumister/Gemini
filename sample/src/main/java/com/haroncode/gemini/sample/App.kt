package com.haroncode.gemini.sample

import android.app.Application
import com.haroncode.gemini.sample.di.AppModule
import com.haroncode.gemini.sample.di.DI
import java.util.*
import timber.log.Timber
import timber.log.Timber.DebugTree
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator

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

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    private fun initAppScope() {
        val openScope = Toothpick.openScope(DI.APP_SCOPE)
        openScope.installModules(AppModule(this))
        Toothpick.inject(this, openScope)
    }

    private fun initToothpick() {
        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(
                Configuration.forDevelopment().preventMultipleRootScopes()
            )
        } else {
            Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
            FactoryRegistryLocator.setRootRegistry(com.haroncode.gemini.FactoryRegistry())
            MemberInjectorRegistryLocator.setRootRegistry(com.haroncode.gemini.MemberInjectorRegistry())
        }
    }
}
