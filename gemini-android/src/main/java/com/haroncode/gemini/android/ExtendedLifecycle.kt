package com.haroncode.gemini.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

/**
 * @author kdk96.
 */
internal interface ExtendedLifecycleObserversHolder {
    fun add(observer: ExtendedLifecycleObserver)
    fun remove(observer: ExtendedLifecycleObserver)
}

internal class ExtendedLifecycle(
    private val lifecycle: Lifecycle,
    private val extendedLifecycleObserversHolder: ExtendedLifecycleObserversHolder
) : Lifecycle() {

    override fun addObserver(observer: LifecycleObserver) {
        if (observer is ExtendedLifecycleObserver) extendedLifecycleObserversHolder.add(observer)
        lifecycle.addObserver(observer)
    }

    override fun removeObserver(observer: LifecycleObserver) {
        if (observer is ExtendedLifecycleObserver) extendedLifecycleObserversHolder.remove(observer)
        lifecycle.removeObserver(observer)
    }

    override fun getCurrentState(): State = lifecycle.currentState
}
