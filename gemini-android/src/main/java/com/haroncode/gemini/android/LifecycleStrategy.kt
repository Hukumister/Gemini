package com.haroncode.gemini.android

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.android.extended.AndroidLifecycleEvent
import com.haroncode.gemini.connector.StoreLifecycle

interface LifecycleStrategy {

    fun handle(
        source: LifecycleOwner,
        lifecycleState: AndroidLifecycleEvent
    ): StoreLifecycle.Event?
}
