package com.haroncode.gemini.android.binder

import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.android.lifecycle.LifecycleStrategy
import com.haroncode.gemini.android.lifecycle.StoreCancelObserver
import com.haroncode.gemini.connector.AutoCancelStoreRule
import com.haroncode.gemini.connector.ComposeConnectionRule
import com.haroncode.gemini.connector.ConnectionRule

internal class SimpleBinder<View : SavedStateRegistryOwner>(
    private val factoryProvider: () -> ConnectionRule.Factory<View>,
    private val lifecycleStrategy: LifecycleStrategy,
) : Binder<View> {

    override fun bind(view: View) {
        val connectionRule = factoryProvider().create(view)

        lifecycleStrategy.connect(view, connectionRule)

        val composeConnectionRule = connectionRule as? ComposeConnectionRule
        val autoCancelStoreRuleCollection = composeConnectionRule?.connectionRules
            .orEmpty()
            .filterIsInstance<AutoCancelStoreRule>()
        view.lifecycle.addObserver(StoreCancelObserver(autoCancelStoreRuleCollection))
    }
}
