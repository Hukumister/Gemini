package com.haroncode.gemini.android.strategies

import androidx.lifecycle.Lifecycle
import com.haroncode.gemini.android.BindingStrategy
import com.haroncode.gemini.core.StoreLifecycle.Event

object ResumePauseStrategy : BindingStrategy {

    override fun handle(lifecycleState: Lifecycle.Event) = when (lifecycleState) {
        Lifecycle.Event.ON_RESUME -> Event.START
        Lifecycle.Event.ON_PAUSE -> Event.STOP
        else -> null
    }
}