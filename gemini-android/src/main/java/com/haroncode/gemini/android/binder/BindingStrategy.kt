package com.haroncode.gemini.android.binder

import androidx.lifecycle.LifecycleObserver
import com.haroncode.gemini.core.StoreLifecycle

typealias StateSender = (StoreLifecycle.State) -> Unit

interface BindingStrategy {

    fun handle(sender: StateSender): LifecycleObserver
}