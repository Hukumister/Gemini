package com.haroncode.gemini.binder

import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.lifecycle.LifecycleStrategy
import com.haroncode.gemini.lifecycle.StartStopStrategy

actual object StoreViewBinding {

    fun <T : SavedStateRegistryOwner> with(
        lifecycleStrategy: LifecycleStrategy = StartStopStrategy,
        factoryProvider: () -> BindingRulesFactory<T>,
    ): Binder<T> = SimpleBinder(
        factoryProvider = factoryProvider,
        lifecycleStrategy = lifecycleStrategy,
    )

    fun <T : SavedStateRegistryOwner> withRestore(
        lifecycleStrategy: LifecycleStrategy = StartStopStrategy,
        factoryProvider: () -> BindingRulesFactory<T>,
    ): Binder<T> = RestoreBinder(
        factoryProvider = factoryProvider,
        lifecycleStrategy = lifecycleStrategy
    )
}
