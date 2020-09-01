package com.haroncode.gemini.android.binder

import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.android.lifecycle.LifecycleStrategy
import com.haroncode.gemini.android.lifecycle.StoreCancelObserver
import com.haroncode.gemini.binder.AutoCancelStoreRule
import com.haroncode.gemini.binder.BaseBindingRule
import com.haroncode.gemini.binder.Binder
import com.haroncode.gemini.binder.BindingRulesFactory

internal class SimpleBinder<View : SavedStateRegistryOwner>(
    private val factoryProvider: () -> BindingRulesFactory<View>,
    private val lifecycleStrategy: LifecycleStrategy,
) : Binder<View> {

    override fun bind(view: View) {
        val bindingRules = factoryProvider().create(view)

        lifecycleStrategy.connect(view, bindingRules.filterIsInstance<BaseBindingRule<*, *>>())

        val autoCancelStoreRuleCollection = bindingRules.filterIsInstance<AutoCancelStoreRule>()
        view.lifecycle.addObserver(StoreCancelObserver(autoCancelStoreRuleCollection))
    }
}
