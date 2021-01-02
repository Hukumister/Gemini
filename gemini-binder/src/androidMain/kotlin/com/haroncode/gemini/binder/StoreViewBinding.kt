package com.haroncode.gemini.binder

import androidx.annotation.CheckResult
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.binder.rule.BindingRulesFactory
import com.haroncode.gemini.lifecycle.strategy.LifecycleStrategy
import com.haroncode.gemini.lifecycle.strategy.StartStopStrategy

actual object StoreViewBinding {

    @Deprecated(
        message = "Will be removed in upcoming release, have to use getStore extension to retain and cancel store instance in ViewModel",
        replaceWith = ReplaceWith("StoreViewBinding.with(factory, lifecycleStrategy)")
    )
    @CheckResult
    fun <T : SavedStateRegistryOwner> with(
        lifecycleStrategy: LifecycleStrategy = StartStopStrategy,
        factoryProvider: () -> BindingRulesFactory<T>,
    ): Binder<T> = SimpleBinder(
        factoryProvider = factoryProvider,
        lifecycleStrategy = lifecycleStrategy,
    )

    @Deprecated(
        message = "Will be removed in upcoming release, have to use getStore extension to retain and cancel store instance in ViewModel",
        replaceWith = ReplaceWith("StoreViewBinding.with(factory, lifecycleStrategy)")
    )
    @CheckResult
    fun <T : SavedStateRegistryOwner> withRestore(
        lifecycleStrategy: LifecycleStrategy = StartStopStrategy,
        factoryProvider: () -> BindingRulesFactory<T>,
    ): Binder<T> = RestoreBinder(
        factoryProvider = factoryProvider,
        lifecycleStrategy = lifecycleStrategy,
    )

    @CheckResult
    fun <T : LifecycleOwner> with(
        factory: BindingRulesFactory<T>,
        lifecycleStrategy: LifecycleStrategy = StartStopStrategy
    ): Binder<T> = BinderImpl(
        factory = factory,
        lifecycleStrategy = lifecycleStrategy
    )
}
