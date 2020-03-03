package com.haroncode.gemini.android.strategies

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.android.LifecycleStrategy
import com.haroncode.gemini.connector.StoreLifecycle.Event
import io.reactivex.Maybe

object StartStopStrategy : LifecycleStrategy {

    override fun handle(
        source: LifecycleOwner,
        lifecycleState: Lifecycle.Event
    ) = when (lifecycleState) {
        Lifecycle.Event.ON_START -> Maybe.just(Event.START)
        Lifecycle.Event.ON_STOP -> Maybe.just(Event.STOP)
        else -> Maybe.empty()
    }
}
