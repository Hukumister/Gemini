package com.haroncode.gemini.sample.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.haroncode.gemini.sample.data.ConnectivityRepositoryImpl
import com.haroncode.gemini.sample.domain.repository.AuthRepository
import com.haroncode.gemini.sample.domain.repository.AuthRepositoryImpl
import com.haroncode.gemini.sample.domain.repository.ConnectivityRepository
import toothpick.config.Module

class AppModule(context: Context) : Module() {
    init {

        // Navigation
        val cicerone = Cicerone.create(Router())
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.getNavigatorHolder())

        // Global
        bind(Context::class.java).toInstance(context)
        bind(ConnectivityRepository::class.java).to(ConnectivityRepositoryImpl::class.java).singleton()
        bind(AuthRepository::class.java).to(AuthRepositoryImpl::class.java).singleton()
    }
}
