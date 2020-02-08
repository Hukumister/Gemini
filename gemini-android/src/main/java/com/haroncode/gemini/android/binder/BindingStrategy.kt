package com.haroncode.gemini.android.binder

import androidx.lifecycle.LifecycleObserver
import com.haroncode.gemini.binder.BinderLifecycle

typealias StateSender = (BinderLifecycle.State) -> Unit

interface BindingStrategy {

    fun handle(sender: StateSender): LifecycleObserver
}