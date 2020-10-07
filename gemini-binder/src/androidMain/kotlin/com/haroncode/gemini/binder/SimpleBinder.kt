package com.haroncode.gemini.binder

import androidx.lifecycle.coroutineScope
import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.binder.coordinator.Coordinator
import com.haroncode.gemini.binder.rule.AutoCancelStoreRule
import com.haroncode.gemini.lifecycle.BindingRuleComposer
import com.haroncode.gemini.lifecycle.StoreCancelObserver
import com.haroncode.gemini.lifecycle.StoreLifecycleObserver
import com.haroncode.gemini.lifecycle.strategy.LifecycleStrategy

internal class SimpleBinder<View : SavedStateRegistryOwner>(
    private val factoryProvider: () -> BindingRulesFactory<View>,
    private val lifecycleStrategy: LifecycleStrategy,
) : Binder<View> {

    override fun bind(view: View) {
        val bindingRules = factoryProvider().create(view)
        val autoCancelStoreRuleCollection = bindingRules.filterIsInstance<AutoCancelStoreRule>()

        val coroutineScope = view.lifecycle.coroutineScope
        val bindingRuleComposer = BindingRuleComposer(coroutineScope, bindingRules)

        val flowLifecycleObserver = StoreLifecycleObserver(lifecycleStrategy, Coordinator(coroutineScope, bindingRuleComposer))
        view.lifecycle.addObserver(flowLifecycleObserver)
        view.lifecycle.addObserver(StoreCancelObserver(autoCancelStoreRuleCollection))
    }
}
