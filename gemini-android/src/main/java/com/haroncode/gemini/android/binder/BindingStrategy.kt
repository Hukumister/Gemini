package com.haroncode.gemini.android.binder

import androidx.lifecycle.Lifecycle
import com.haroncode.gemini.core.StoreLifecycle

interface BindingStrategy {

    fun handle(lifecycleState: Lifecycle.Event): StoreLifecycle.Event?
}