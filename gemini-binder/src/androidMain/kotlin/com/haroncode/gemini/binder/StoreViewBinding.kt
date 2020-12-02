package com.haroncode.gemini.binder

import androidx.annotation.CheckResult
import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.binder.rule.BindingRulesFactory
import com.haroncode.gemini.lifecycle.strategy.LifecycleStrategy
import com.haroncode.gemini.lifecycle.strategy.StartStopStrategy

actual object StoreViewBinding {

    @CheckResult
    fun <T : SavedStateRegistryOwner> with(
        lifecycleStrategy: LifecycleStrategy = StartStopStrategy,
        factoryProvider: () -> BindingRulesFactory<T>,
    ): Binder<T> = SimpleBinder(
        factoryProvider = factoryProvider,
        lifecycleStrategy = lifecycleStrategy,
    )

    @CheckResult
    fun <T : SavedStateRegistryOwner> withRestore(
        lifecycleStrategy: LifecycleStrategy = StartStopStrategy,
        factoryProvider: () -> BindingRulesFactory<T>,
    ): Binder<T> = RestoreBinder(
        factoryProvider = factoryProvider,
        lifecycleStrategy = lifecycleStrategy
    )
}
