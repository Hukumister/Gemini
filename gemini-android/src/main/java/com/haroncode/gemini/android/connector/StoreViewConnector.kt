package com.haroncode.gemini.android.connector

import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.android.lifecycle.LifecycleStrategy
import com.haroncode.gemini.android.lifecycle.StartStopStrategy
import com.haroncode.gemini.android.lifecycle.StoreCancelObserver
import com.haroncode.gemini.connector.AutoCancelStoreRule
import com.haroncode.gemini.connector.BaseConnectionRule
import com.haroncode.gemini.connector.ConnectionRulesFactory

/**
 * @author HaronCode
 * @author kdk96
 */
object StoreViewConnector {

    fun <T : SavedStateRegistryOwner> withFactory(factory: ConnectionRulesFactory<T>) = ConnectionProcessor(factory)

    class ConnectionProcessor<T : SavedStateRegistryOwner>(private val factory: ConnectionRulesFactory<T>) {

        private var lifecycleStrategy: LifecycleStrategy = StartStopStrategy

        fun withStrategy(lifecycleStrategy: LifecycleStrategy): ConnectionProcessor<T> {
            this.lifecycleStrategy = lifecycleStrategy
            return this
        }

        fun connect(view: T) {
            val connectionRules = factory.create(view)

            lifecycleStrategy.connect(view, connectionRules.filterIsInstance<BaseConnectionRule<*, *>>())

            val autoCancelStoreRuleCollection = connectionRules.filterIsInstance<AutoCancelStoreRule>()
            view.lifecycle.addObserver(StoreCancelObserver(autoCancelStoreRuleCollection))
        }
    }
}
