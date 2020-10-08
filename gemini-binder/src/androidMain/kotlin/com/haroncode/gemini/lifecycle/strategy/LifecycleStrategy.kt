package com.haroncode.gemini.lifecycle.strategy

import androidx.lifecycle.Lifecycle
import com.haroncode.gemini.binder.coordinator.StoreLifecycleEvent

/**
 * @author HaronCode
 * @author kdk96
 */
interface LifecycleStrategy {

    fun map(lifecycle: Lifecycle.Event): StoreLifecycleEvent?
}
