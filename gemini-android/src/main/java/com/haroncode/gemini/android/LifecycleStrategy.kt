package com.haroncode.gemini.android

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.connector.StoreLifecycle
import io.reactivex.MaybeSource

interface LifecycleStrategy {

    fun handle(
        source: LifecycleOwner,
        lifecycleState: Lifecycle.Event
    ): MaybeSource<StoreLifecycle.Event>
}
