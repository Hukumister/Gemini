package com.haroncode.gemini.android.binder

import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.android.lifecycle.LifecycleStrategy
import com.haroncode.gemini.android.lifecycle.StoreCancelObserver
import com.haroncode.gemini.connector.AutoCancelStoreRule
import com.haroncode.gemini.connector.BaseConnectionRule
import com.haroncode.gemini.connector.ConnectionRulesFactory

internal class SimpleBinder<View : SavedStateRegistryOwner>(
    private val factoryProvider: () -> ConnectionRulesFactory<View>,
    private val lifecycleStrategy: LifecycleStrategy,
) : Binder<View> {

    override fun bind(view: View) {
        val connectionRules = factoryProvider().create(view)

        lifecycleStrategy.connect(view, connectionRules.filterIsInstance<BaseConnectionRule<*, *>>())

        val autoCancelStoreRuleCollection = connectionRules.filterIsInstance<AutoCancelStoreRule>()
        view.lifecycle.addObserver(StoreCancelObserver(autoCancelStoreRuleCollection))
    }
}
