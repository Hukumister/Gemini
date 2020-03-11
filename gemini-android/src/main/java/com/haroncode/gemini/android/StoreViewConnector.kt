package com.haroncode.gemini.android

import androidx.savedstate.SavedStateRegistryOwner
import com.haroncode.gemini.android.lifecycle.StoreLifecycleEventObserver
import com.haroncode.gemini.android.strategies.StartStopStrategy
import com.haroncode.gemini.connection.ConnectionRule
import com.haroncode.gemini.connector.BaseStoreConnector

object StoreViewConnector {

    fun <T : SavedStateRegistryOwner> withFactory(factory: ConnectionRule.Factory<T>) = ConnectionProcessor(factory)

    class ConnectionProcessor<T : SavedStateRegistryOwner>(private val factory: ConnectionRule.Factory<T>) {

        private var lifecycleStrategy: LifecycleStrategy = StartStopStrategy

        fun withStrategy(lifecycleStrategy: LifecycleStrategy): ConnectionProcessor<T> {
            this.lifecycleStrategy = lifecycleStrategy
            return this
        }

        fun connect(view: T) {
            val connectionRules = factory.create(view)
            val storeConnector = BaseStoreConnector(connectionRules)

            val storeLifecycle = StoreLifecycleEventObserver(view, storeConnector, lifecycleStrategy)
            view.lifecycle.addObserver(storeLifecycle)
        }
    }
}
