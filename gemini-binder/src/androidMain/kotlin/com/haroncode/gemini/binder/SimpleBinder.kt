package com.haroncode.gemini.binder

import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.binder.coordinator.Coordinator
import com.haroncode.gemini.binder.rule.AutoCancelStoreRule
import com.haroncode.gemini.binder.rule.BindingRulesFactory
import com.haroncode.gemini.lifecycle.StoreCancelObserver
import com.haroncode.gemini.lifecycle.StoreLifecycleObserver
import com.haroncode.gemini.lifecycle.strategy.LifecycleStrategy
import kotlinx.coroutines.Dispatchers

@Deprecated(
    message = "Will be removed in upcoming release, have to use getStore extension to retain and cancel store instance in ViewModel",
    replaceWith = ReplaceWith("getStore(provider)", imports = ["com.haroncode.gemini.keeper.getStore"])
)
internal class SimpleBinder<View : SavedStateRegistryOwner>(
    private val factoryProvider: () -> BindingRulesFactory<View>,
    private val lifecycleStrategy: LifecycleStrategy,
) : Binder<View> {

    override fun bind(view: View) {
        val bindingRules = factoryProvider().create(view)
        val autoCancelStoreRuleCollection = bindingRules.filterIsInstance<AutoCancelStoreRule>()

        val storeLifecycleObserver = StoreLifecycleObserver(lifecycleStrategy, Coordinator(Dispatchers.Main.immediate, bindingRules))
        view.lifecycle.addObserver(storeLifecycleObserver)
        view.lifecycle.addObserver(StoreCancelObserver(autoCancelStoreRuleCollection))
    }
}
