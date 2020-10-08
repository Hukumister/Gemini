package com.haroncode.gemini.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.binder.coordinator.Coordinator
import com.haroncode.gemini.lifecycle.strategy.LifecycleStrategy

internal class StoreLifecycleObserver(
    private val lifecycleStrategy: LifecycleStrategy,
    private val coordinator: Coordinator
) : LifecycleEventObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        lifecycleStrategy.map(event)?.let(coordinator::accept)
    }
}
