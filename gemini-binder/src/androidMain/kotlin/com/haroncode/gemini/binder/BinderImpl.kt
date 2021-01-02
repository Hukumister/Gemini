package com.haroncode.gemini.binder

import androidx.lifecycle.LifecycleOwner
import com.haroncode.gemini.binder.coordinator.Coordinator
import com.haroncode.gemini.binder.rule.BindingRulesFactory
import com.haroncode.gemini.lifecycle.StoreLifecycleObserver
import com.haroncode.gemini.lifecycle.strategy.LifecycleStrategy
import kotlinx.coroutines.Dispatchers

internal class BinderImpl<View : LifecycleOwner>(
    private val factory: BindingRulesFactory<View>,
    private val lifecycleStrategy: LifecycleStrategy
) : Binder<View> {

    override fun bind(view: View) {
        val bindingRules = factory.create(view)
        val coordinator = Coordinator(Dispatchers.Main.immediate, bindingRules)
        val storeLifecycleObserver = StoreLifecycleObserver(lifecycleStrategy, coordinator)
        view.lifecycle.addObserver(storeLifecycleObserver)
    }
}
