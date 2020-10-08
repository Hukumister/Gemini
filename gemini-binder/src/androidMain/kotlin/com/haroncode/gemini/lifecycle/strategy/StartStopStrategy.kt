package com.haroncode.gemini.lifecycle.strategy

import androidx.lifecycle.Lifecycle
import com.haroncode.gemini.binder.coordinator.StoreLifecycleEvent

/**
 * @author HaronCode
 * @author kdk96
 */
object StartStopStrategy : LifecycleStrategy {

    override fun map(lifecycle: Lifecycle.Event): StoreLifecycleEvent? = when (lifecycle) {
        Lifecycle.Event.ON_START -> StoreLifecycleEvent.ON_ATTACH
        Lifecycle.Event.ON_STOP -> StoreLifecycleEvent.ON_DETACH
        else -> null
    }
}
