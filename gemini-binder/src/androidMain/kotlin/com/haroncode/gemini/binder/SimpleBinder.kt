package com.haroncode.gemini.binder

import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.lifecycle.LifecycleStrategy
import com.haroncode.gemini.lifecycle.StoreCancelObserver

internal class SimpleBinder<View : SavedStateRegistryOwner>(
    private val factoryProvider: () -> BindingRulesFactory<View>,
    private val lifecycleStrategy: LifecycleStrategy,
) : Binder<View> {

    override fun bind(view: View) {
        val bindingRules = factoryProvider().create(view)

        lifecycleStrategy.bind(view, bindingRules.filterIsInstance<BaseBindingRule<*, *>>())

        val autoCancelStoreRuleCollection = bindingRules.filterIsInstance<AutoCancelStoreRule>()
        view.lifecycle.addObserver(StoreCancelObserver(autoCancelStoreRuleCollection))
    }
}
