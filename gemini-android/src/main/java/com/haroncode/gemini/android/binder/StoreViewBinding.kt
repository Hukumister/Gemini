package com.haroncode.gemini.android.binder

import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.android.lifecycle.LifecycleStrategy
import com.haroncode.gemini.android.lifecycle.StartStopStrategy
import com.haroncode.gemini.binder.Binder
import com.haroncode.gemini.binder.BindingRulesFactory

/**
 * @author HaronCode
 * @author kdk96
 */
object StoreViewBinding {

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
