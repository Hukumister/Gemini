package com.haroncode.gemini.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

internal interface RealRemovingObserversHolder {
    fun add(observer: ExtendedLifecycleObserver)
    fun remove(observer: ExtendedLifecycleObserver)
}

internal class ExtendedLifecycle(
    private val lifecycle: Lifecycle,
    private val realRemovingObserversHolder: RealRemovingObserversHolder
) : Lifecycle() {

    override fun addObserver(observer: LifecycleObserver) {
        if (observer is ExtendedLifecycleObserver) realRemovingObserversHolder.add(observer)
        lifecycle.addObserver(observer)
    }

    override fun removeObserver(observer: LifecycleObserver) {
        if (observer is ExtendedLifecycleObserver) realRemovingObserversHolder.remove(observer)
        lifecycle.removeObserver(observer)
    }

    override fun getCurrentState(): State = lifecycle.currentState
}
