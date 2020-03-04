package com.haroncode.gemini.android.strategies

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.android.LifecycleStrategy
import com.haroncode.gemini.connector.StoreLifecycle.Event

object ResumePauseStrategy : LifecycleStrategy {

    override fun handle(
        source: LifecycleOwner,
        lifecycleState: Lifecycle.Event
    ) = when (lifecycleState) {
        Lifecycle.Event.ON_RESUME -> Event.START
        Lifecycle.Event.ON_PAUSE -> Event.STOP
        else -> null
    }
}
