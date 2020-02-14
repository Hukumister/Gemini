package com.haroncode.gemini.android.binder.strategies

import androidx.lifecycle.Lifecycle
import com.haroncode.gemini.android.binder.BindingStrategy
import com.haroncode.gemini.core.StoreLifecycle.Event

object StartStopStrategy : BindingStrategy {

    override fun handle(lifecycleState: Lifecycle.Event) = when (lifecycleState) {
        Lifecycle.Event.ON_START -> Event.START
        Lifecycle.Event.ON_STOP -> Event.STOP
        else -> null
    }
}